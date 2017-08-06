package com.newbig.app.imagesearch.service.impl;

import com.google.common.collect.Lists;
import com.newbig.app.imagesearch.service.ImageSearchService;
import com.newbig.app.imagesearch.service.lire.SearchResultsTableModel;
import net.semanticmetadata.lire.imageanalysis.features.global.CEDD;
import net.semanticmetadata.lire.imageanalysis.features.global.ColorLayout;
import net.semanticmetadata.lire.searchers.GenericFastImageSearcher;
import net.semanticmetadata.lire.searchers.ImageSearchHits;
import net.semanticmetadata.lire.searchers.ImageSearcher;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;


@Service("imageSearchService")
public class ImageSearchServiceImpl implements ImageSearchService {
    @Override
    public List<String> searchByImage(MultipartFile file) {
        try {
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("index")));
            ImageSearcher searcher = getSearcher(5);
            ImageSearchHits hits = searcher.search(file.getInputStream(), reader);
            SearchResultsTableModel tableModel = new SearchResultsTableModel();
            List<String> ls=tableModel.setHits1(hits, reader);
            reader.close();
            return ls;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Lists.newArrayList();
    }

    private static ImageSearcher getSearcher(int numResults) {
        ImageSearcher searcher = new GenericFastImageSearcher(numResults, ColorLayout.class);
//        if (selectboxDocumentBuilder.getSelectedIndex() == 1) {
//            searcher = new GenericFastImageSearcher(numResults, ScalableColor.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 2) {
//            searcher = new GenericFastImageSearcher(numResults, EdgeHistogram.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 3) {
//            searcher = new GenericFastImageSearcher(numResults, AutoColorCorrelogram.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 4) { // CEDD
        searcher = new GenericFastImageSearcher(numResults, CEDD.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 5) { // FCTH
//            searcher = new GenericFastImageSearcher(numResults, FCTH.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 6) { // JCD
//            searcher = new GenericFastImageSearcher(numResults, JCD.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 7) { // SimpleColorHistogram
//            searcher = new GenericFastImageSearcher(numResults, SimpleColorHistogram.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 8) {
//            searcher = new GenericFastImageSearcher(numResults, Tamura.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 9) {
//            searcher = new GenericFastImageSearcher(numResults, Gabor.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 10) {
//            searcher = new GenericFastImageSearcher(numResults, JpegCoefficientHistogram.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 11) {
////            searcher = new VisualWordsImageSearcher(numResults, DocumentBuilder.FIELD_NAME_SURF + DocumentBuilder.FIELD_NAME_BOVW);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 12) {
//            searcher = new GenericFastImageSearcher(numResults, JointHistogram.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 13) {
//            searcher = new GenericFastImageSearcher(numResults, OpponentHistogram.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 14) {
//            searcher = new GenericFastImageSearcher(numResults, LuminanceLayout.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 15) {
//            searcher = new GenericFastImageSearcher(numResults, PHOG.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() == 16) {
//            searcher = new GenericFastImageSearcher(numResults, ACCID.class);
//        } else if (selectboxDocumentBuilder.getSelectedIndex() >= 17) {
//            searcher = new GenericFastImageSearcher(numResults, COMO.class);
//        }
        return searcher;
    }
}
