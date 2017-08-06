package com.newbig.app.face2face.auth.starter;

import com.newbig.app.face2face.auth.AuthLogicConnection;
import com.newbig.app.face2face.auth.AuthServer;
import com.newbig.app.face2face.auth.Worker;
import com.newbig.app.face2face.thirdparty.redis.utils.RedisPoolManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;

@Slf4j
public class AuthStarter {
    public static RedisPoolManager redisPoolManager;
    public static int workNum = 1;
    public static Worker[] workers;
    private static File cfg = null;

    public static void main(String[] args) throws Exception {

        configureAndStart(args);
    }

    static void configureAndStart(String[] args) throws ParseException {
        parseArgs(args);

        try {
            //parse xml File and apply it
            DocumentBuilder builder = DocumentBuilderFactory
                .newInstance().newDocumentBuilder();
            Document doc = builder.parse(cfg);
            Element rootElement = doc.getDocumentElement();

            XPath xPath = XPathFactory.newInstance().newXPath();
            XPathExpression xPathExpression = null;
            NodeList nodeList = null;
            Element element = null;


            xPathExpression = xPath.compile("/auth/authserver");
            nodeList = (NodeList) xPathExpression.evaluate(rootElement, XPathConstants.NODESET);
            element = (Element) nodeList.item(0);
            int authListenPort = Integer.parseInt(element.getAttribute("port"));
            workNum = Integer.parseInt(element.getAttribute("workNum"));
            Worker.startWorker(workNum);
            log.info("Authserver authListenPort " + authListenPort);

            xPathExpression = xPath.compile("/auth/redis");
            nodeList = (NodeList) xPathExpression.evaluate(rootElement, XPathConstants.NODESET);
            element = (Element) nodeList.item(0);
            redisPoolManager = new RedisPoolManager();
            redisPoolManager.REDIS_SERVER = element.getAttribute("ip");
            redisPoolManager.REDIS_PORT = Integer.parseInt(element.getAttribute("port"));

            redisPoolManager.returnJedis(redisPoolManager.getJedis());
            log.info("Redis init successed");

            xPathExpression = xPath.compile("/auth/logic");
            nodeList = (NodeList) xPathExpression.evaluate(rootElement, XPathConstants.NODESET);
            element = (Element) nodeList.item(0);
            String logicIp = element.getAttribute("ip");
            int logicPort = Integer.parseInt(element.getAttribute("port"));

            //Now Start Servers
            new Thread(() -> AuthServer.startAuthServer(authListenPort)).start();

            new Thread(() -> AuthLogicConnection.startAuthLogicConnection(logicIp, logicPort)).start();

        } catch (Exception e) {
            log.error("init cfg error");
            e.printStackTrace();
        }
        //init log
    }

    static void parseArgs(String[] args) throws ParseException {
        // Create a Parser
        CommandLineParser parser = new BasicParser();
        Options options = new Options();
        options.addOption("h", "help", false, "Print this usage information");
        options.addOption("c", "cfg", true, "config Absolute Path");
        options.addOption("l", "log", true, "log configuration");

        // Parse the program arguments
        CommandLine commandLine = parser.parse(options, args);
        // Set the appropriate variables based on supplied options

        if (commandLine.hasOption('h')) {
            printHelpMessage();
            System.exit(0);
        }
        cfg = new File("/work/NewWork/java/app/src/main/resources/conf/auth.xml");
    }

    static void printHelpMessage() {
        System.out.println("Change the xml File and Log.XML Path to the right Absolute Path base on your project Location in your computor");
        System.out.println("Usage example: ");
        System.out.println("java -cfg D:\\MyProject\\face2face\\auth\\src\\main\\resources\\auth.xml  -log D:\\MyProject\\face2face\\auth\\src\\main\\resources\\log.xml");
        System.exit(0);
    }

}
