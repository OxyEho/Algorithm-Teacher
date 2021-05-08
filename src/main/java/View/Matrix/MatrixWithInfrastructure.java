package View.Matrix;

import org.apache.commons.lang3.tuple.Pair;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class MatrixWithInfrastructure extends JPanel {
    private final JScrollPane scrollPane;
    private final JTable table;
    private static final int CELL_SIZE = 60;
    private static final int xShift = 80;
    private static final int yShift = 85;

    public MatrixWithInfrastructure(int parentWidth, int parentHeight, int tableSize,
                                    ActionListener sizeListener, ActionListener matrixListener,
                                    List<String> nodes, List<Pair<String, String>> pairs) {
        scrollPane = new JScrollPane();
        setBounds(xShift, yShift, parentWidth - xShift, parentHeight - yShift);
        table = new JTable(tableSize, tableSize) {
            private final DigitalCellEditor digitalCellEditor = new DigitalCellEditor();
            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (row >= 1 && row < this.getWidth() && column >= 1 && column < this.getHeight())
                    return digitalCellEditor;
                else
                    return super.getCellEditor(row, column);
            }
        };
        JTextField sizeField = new JTextField();
        sizeField.addActionListener(sizeListener);
        JButton button = new JButton("Показать граф");
        button.addActionListener(matrixListener);
        configureTable(tableSize, nodes, pairs);
        JList<String> rowHeader = getRowHeaders(nodes);
        rowHeader.setFixedCellHeight(CELL_SIZE);
        scrollPane.setRowHeaderView(rowHeader);
        createLayout(new JLabel("Размер: "), sizeField, button);
    }

    private void configureTable(int tableSize, List<String> nodes, List<Pair<String, String>> pairs) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setSize(new Dimension(CELL_SIZE * tableSize, CELL_SIZE * tableSize));

        setCellSizes();
        setColumnHeaders(nodes);
        setTableValues(tableSize, nodes, pairs);
        scrollPane.getViewport().add(table);
        table.setRowSelectionAllowed(false);
        table.getTableHeader().setEnabled(false);
        table.setFont(new Font("Microsoft JhengHei", Font.BOLD, 26));
    }

    private void createLayout(JLabel sizeLabel, JTextField sizeField, JButton button){
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup().addGroup(
                        layout.createSequentialGroup()
                                .addComponent(sizeLabel).addComponent(sizeField).addComponent(button)
                ).addComponent(scrollPane)
        );
        layout.linkSize(sizeField, sizeLabel);
        layout.setVerticalGroup(
                layout.createSequentialGroup().addGroup(
                        layout.createParallelGroup()
                                .addComponent(sizeLabel).addComponent(sizeField).addComponent(button)
                ).addComponent(scrollPane)
        );
    }

    public JTable getTable() { return table; }

    public JScrollPane getScrollPane() { return scrollPane; }

    public JList<String> getRowHeaders(java.util.List<String> nodes) {
        nodes.add(0, "");
        ListModel<String> listModel = new AbstractListModel<>() {
            final String[] headers = nodes.toArray(String[]::new);
            public int getSize() { return headers.length; }
            public String getElementAt(int index) {
                return headers[index];
            }
        };

        return new JList<>(listModel);
    }

    public void setColumnHeaders(java.util.List<String> nodes) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i == 0)
                table.getColumnModel().getColumn(i).setHeaderValue("");
            else
                table.getColumnModel().getColumn(i).setHeaderValue(nodes.get(i-1));
        }
    }

    public void setCellSizes() {
        table.setRowHeight(CELL_SIZE);
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setMaxWidth(CELL_SIZE);
        }
    }

    public void setTableValues(int size, java.util.List<String> nodes, List<Pair<String, String>> pairs) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < size - 1; i++) {
            model.setValueAt(nodes.get(i), 0, i+1);
            model.setValueAt(nodes.get(i), i+1, 0);
        }
        for (int i = 1; i < size; i++) {
            for (int j = 1; j < size; j++) {
                model.setValueAt(0, i, j);
            }
        }

        for (Pair<String, String> pair: pairs) {
            int x = nodes.indexOf(pair.getLeft());
            int y = nodes.indexOf(pair.getRight());
            model.setValueAt(1, x + 1, y + 1);
        }
    }

//    private void initTable
}
