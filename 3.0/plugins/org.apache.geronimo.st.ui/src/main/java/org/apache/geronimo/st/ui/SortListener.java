/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.geronimo.st.ui;

import java.text.Collator;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * @version $Rev$ $Date$
 * 
 * "sort a table by column" snippet from http://www.eclipse.org/swt/snippets/
 * 
 */
public class SortListener implements Listener {
    protected Table table;
    protected String[] columnNames;

    public SortListener(Table table, String[] columnNames) {
        this.table = table;
        this.columnNames = columnNames;
    }

    public void handleEvent(Event e) {
        TableItem[] tableItems = table.getItems();
        Collator collator = Collator.getInstance(Locale.getDefault());
        TableColumn column = (TableColumn) e.widget;
        int columnIndex = 0;
        int direction = 0;
        for (int i = 0; i < columnNames.length; ++i) {
            if (column.getText().equals(columnNames[i])) {
                table.setSortColumn(column);
                direction = table.getSortDirection() == SWT.UP ? SWT.DOWN : SWT.UP;
                table.setSortDirection(direction);
                columnIndex = i;
                break;
            }
        }
        // sort the table using bubble sort technique
        for (int i = 1; i < tableItems.length; i++) {
            String value1 = tableItems[i].getText(columnIndex);
            for (int j = 0; j < i; j++) {
                String value2 = tableItems[j].getText(columnIndex);
                boolean notSorted = false;
                if (direction == SWT.UP && collator.compare(value1, value2) > 0) {
                    notSorted = true;
                }
                if (direction == SWT.DOWN && collator.compare(value1, value2) < 0) {
                    notSorted = true;
                }
                if (notSorted) {
                    String[] values = new String[columnNames.length];
                    for (int col = 0; col < columnNames.length; ++col) {
                        values[col] = tableItems[i].getText(col);
                    }
                    tableItems[i].dispose();
                    TableItem item = new TableItem(table, SWT.NONE, j);
                    item.setText(values);
                    tableItems = table.getItems();
                    break;
                }
            }
        }
    }
}
