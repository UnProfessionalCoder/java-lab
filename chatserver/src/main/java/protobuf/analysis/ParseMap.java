package protobuf.analysis;

import com.google.protobuf.Message;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
public class ParseMap {

    @FunctionalInterface
    public interface Parsing{
        Message  process(byte[] bytes) throws IOException;
    }

    public static HashMap<Integer, Parsing> parseMap = new HashMap<>();
    public static HashMap<Class<?>, Integer> msg2ptoNum = new HashMap<>();

    public static void register(int ptoNum, Parsing parse, Class<?> cla) {
        if (parseMap.get(ptoNum) == null)
            parseMap.put(ptoNum, parse);
        else {
            log.error("pto has been registered in parseMap, ptoNum: {}", ptoNum);
            return;
        }

        if(msg2ptoNum.get(cla) == null)
            msg2ptoNum.put(cla, ptoNum);
        else {
            log.error("pto has been registered in msg2ptoNum, ptoNum: {}", ptoNum);
            return;
        }
    }

    public static Message getMessage(int ptoNum, byte[] bytes) throws IOException {
        Parsing parser = parseMap.get(ptoNum);
        if(parser == null) {
            log.error("UnKnown Protocol Num: {}", ptoNum);
        }
        Message msg = parser.process(bytes);

        return msg;
    }

    public static Integer getPtoNum(Message msg) {
        return getPtoNum(msg.getClass());
    }

    public static Integer getPtoNum(Class<?> clz) {
        return msg2ptoNum.get(clz);
    }

}
