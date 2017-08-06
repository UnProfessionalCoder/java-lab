/*
 * This file is part of the LIRe project: http://lire-project.net
 * LIRe is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * LIRe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LIRe; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * We kindly ask you to refer the following paper in any publication mentioning Lire:
 *
 * Lux Mathias, Savvas A. Chatzichristofis. Lire: Lucene Image Retrieval –
 * An Extensible Java CBIR Library. In proceedings of the 16th ACM International
 * Conference on Multimedia, pp. 1085-1088, Vancouver, Canada, 2008
 *
 * http://doi.acm.org/10.1145/1459359.1459577
 *
 * Copyright statement:
 * ~~~~~~~~~~~~~~~~~~~~
 * (c) 2002-2011 by Mathias Lux (mathias@juggle.at)
 *     http://www.semanticmetadata.net/lire
 */

/*
 * SearchResultsTableModel.java
 *
 * Created on 20. Februar 2007, 12:15
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.newbig.app.imagesearch.service.lire;

import lombok.extern.slf4j.Slf4j;
import net.semanticmetadata.lire.builders.DocumentBuilder;
import net.semanticmetadata.lire.searchers.ImageSearchHits;
import org.apache.lucene.index.IndexReader;

import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Slf4j
public class SearchResultsTableModel  {
    DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
    ImageSearchHits hits = null;
    private List<String> icons;

    /**
     * Creates a new instance of SearchResultsTableModel
     */
    public SearchResultsTableModel() {
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
    }

    public int getColumnCount() {
        return 3;
    }

    public String getColumnName(int col) {
        if (col == 0) {
            return "Preview";
        } else if (col == 1) {
            return "Preview";
        } else if (col == 2) {
            return "Preview";
        }
        return "";
    }


    /**
     * @param hits
     * @param reader
     */
    public void setHits(ImageSearchHits hits, IndexReader reader) {
        this.hits = hits;
        icons = new ArrayList<String>(hits.length());
        for (int i = 0; i < hits.length(); i++) {
            String icon = null;
            try {
                BufferedImage img = null;
                String fileIdentifier = reader.document(hits.documentID(i)).getField(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue();
                icons.add(fileIdentifier);
                log.info(fileIdentifier);
            } catch (Exception ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            }
            icons.add(icon);
        }
    }
    public List<String> setHits1(ImageSearchHits hits, IndexReader reader) {
        this.hits = hits;
        icons = new ArrayList<String>(hits.length());
        for (int i = 0; i < hits.length(); i++) {
            try {
                String fileIdentifier = reader.document(hits.documentID(i)).getField(DocumentBuilder.FIELD_NAME_IDENTIFIER).stringValue();
                icons.add(fileIdentifier.replace("/work/tessdata/testimage/","/image/tfs/"));
            } catch (Exception ex) {
                Logger.getLogger("global").log(Level.SEVERE, null, ex);
            }
        }
        return icons;
    }

    /**
     * @return
     */
    public ImageSearchHits getHits() {
        return hits;
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

}
