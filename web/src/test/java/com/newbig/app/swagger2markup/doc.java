package com.newbig.app.swagger2markup;

import com.google.common.collect.Maps;
import io.github.swagger2markup.Swagger2MarkupConfig;
import io.github.swagger2markup.Swagger2MarkupConverter;
import io.github.swagger2markup.builder.Swagger2MarkupConfigBuilder;
import io.github.swagger2markup.markup.builder.MarkupLanguage;
import org.apache.commons.io.FileUtils;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.OptionsBuilder;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.asciidoctor.Asciidoctor.Factory.create;

/**
 * Created by xiaofan on 17-6-10.
 */
@Ignore
public class doc {
    @Test
    public void toMarkdown(){
//        Path outputDirectory = Paths.get("/work/doc");
//        FileUtils.deleteQuietly(outputDirectory.toFile());

        Swagger2MarkupConverter.from(URI.create("http://127.0.0.1:8688/v2/api-docs")).build()
                .toFile(Paths.get("/work/doc/ab"));

        Swagger2MarkupConfig config = new Swagger2MarkupConfigBuilder()
                .withMarkupLanguage(MarkupLanguage.ASCIIDOC)
                .withInterDocumentCrossReferences()
                .build();

        Swagger2MarkupConverter.from(URI.create("http://127.0.0.1:8688/v2/api-docs")).withConfig(config).build()
                .toFile(Paths.get("/work/doc/a"));
    }

    @Test
    public void toHtml(){
//        final File out = new File("/work/doc/", "aa.pdf");
        Asciidoctor asciidoctor = create();
        final OptionsBuilder optionsBuilder = OptionsBuilder.options();
        Map<String,Object> mss = Maps.newHashMap();
        mss.put("toc","left");
        mss.put("toclevels","3");
        mss.put("icons","font");
        mss.put("setanchors","");
        mss.put("idprefix","1");
        mss.put("idseparator","-");
        mss.put("docinfo1","");
        optionsBuilder.backend("html5")
                        .attributes(mss);
        String html = asciidoctor.convertFile(
                new File("/work/doc/ab.adoc"),
                optionsBuilder);
        System.out.println(html);
    }

}
