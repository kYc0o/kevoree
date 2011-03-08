/**
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kevoree.tools.ui.editor.standalone;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;

import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.MultiSplitLayout;
import org.kevoree.tools.ui.editor.KevoreeEditor;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                JFrame jframe = new JFrame("Kevoree Editor");
                jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                jframe.setPreferredSize(new Dimension(800, 600));
                KevoreeEditor artpanel = new KevoreeEditor();

                jframe.setJMenuBar(artpanel.getMenuBar());

                // jframe.add(artpanel.getPanel(), BorderLayout.CENTER);


                // jframe.add(new LogPanel(), BorderLayout.SOUTH);

                /*
              String layoutDef =
                      "(COLUMN (LEAF name=center weight=0.95) (LEAF name=bottom weight=0.05))";
              MultiSplitLayout.Node modelRoot =
                      MultiSplitLayout.parseModel(layoutDef);
              JXMultiSplitPane multiSplitPane = new JXMultiSplitPane();
              multiSplitPane.getMultiSplitLayout().setModel(modelRoot);

              multiSplitPane.add(artpanel.getPanel(), "center");
              multiSplitPane.add(new LogPanel(), "bottom");
                */

                JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                        artpanel.getPanel(), new LogPanel());
                //splitPane.setResizeWeight(0.3);
                splitPane.setOneTouchExpandable(true);
                splitPane.setContinuousLayout(true);
                splitPane.setDividerSize(7);
                splitPane.setDividerLocation(500);
                splitPane.setResizeWeight(1.0);


                jframe.add(splitPane, BorderLayout.CENTER);
                jframe.pack();
                jframe.setVisible(true);
            }
        });


    }
}
