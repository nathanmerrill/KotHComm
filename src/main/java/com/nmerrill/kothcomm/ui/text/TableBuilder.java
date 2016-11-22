package com.nmerrill.kothcomm.ui.text;

import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.list.primitive.MutableIntList;
import org.eclipse.collections.api.map.primitive.MutableIntObjectMap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.primitive.IntLists;
import org.eclipse.collections.impl.factory.primitive.IntObjectMaps;

import java.util.Arrays;

public class TableBuilder {
    public enum BorderType {
        NONE, ASCII
    }

    public enum BorderPosition  {
        TOP,
        BOTTOM,
        LEFT,
        RIGHT,
        HORIZONTAL,
        VERTICAL,
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        MIDDLE_LEFT,
        MIDDLE_CENTER,
        MIDDLE_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT
    }

    private char noneBorder(BorderPosition position){
        switch (position){
            case VERTICAL:
            case TOP_CENTER:
            case MIDDLE_CENTER:
            case BOTTOM_CENTER:
                return ' ';
            default:
                return '\u0000';
        }
    }

    private char asciiBorder(BorderPosition position){
        switch (position){
            case TOP:
            case HORIZONTAL:
            case BOTTOM:
                return '-';
            case LEFT:
            case RIGHT:
            case VERTICAL:
                return '|';
            default:
                return '+';
        }
    }

    private BorderType borderType;
    private MutableIntObjectMap<ColumnSetting> columnSettings;
    private final ColumnSetting defaults;
    private boolean hasHeader;

    public TableBuilder(){
        borderType = BorderType.NONE;
        this.columnSettings = IntObjectMaps.mutable.empty();
        this.defaults = new ColumnSetting();
        this.hasHeader = false;
    }

    public void hasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public boolean hasHeader(){
        return hasHeader;
    }

    public void setBorderType(BorderType borderType) {
        this.borderType = borderType;
    }

    public void leftAlign(int column){
        getSetting(column).leftAligned = true;
    }

    public void leftAlign(){
        defaults.leftAligned = true;
    }

    public void rightAlign(int column){
        getSetting(column).leftAligned = false;
    }

    public void rightAlign(){
        defaults.leftAligned = false;
    }

    public void setLeftPadding(int padding, int column){
        getSetting(column).leftPadding = padding;
    }

    public void setLeftPadding(int padding){
        defaults.leftPadding = padding;
    }

    private void setRightPadding(int padding, int column){
        getSetting(column).rightPadding = padding;
    }

    public void setRightPadding(int padding){
        defaults.rightPadding = padding;
    }

    private ColumnSetting getSetting(int column){
        return columnSettings.getIfAbsentPut(column, defaults::copy);
    }

    public String display(MutableList<MutableList<String>> table){
        MutableIntList columnWidths = IntLists.mutable.empty();
        for (MutableList<String> row: table){
            for (Pair<String, Integer> column: row.zipWithIndex()){
                int columnIndex = column.getTwo();
                int width = column.getOne().length();
                if (columnWidths.size() == columnIndex){
                    columnWidths.add(width);
                } else {
                    columnWidths.set(columnIndex, Math.max(columnWidths.get(columnIndex), width));
                }
            }
        }
        StringBuilder builder = new StringBuilder();
        doRow(builder, columnWidths, BorderPosition.TOP_LEFT, BorderPosition.TOP, BorderPosition.TOP_CENTER, BorderPosition.TOP_RIGHT);
        boolean first = true;
        for (MutableList<String> row: table){
            doRow(builder, columnWidths, row, BorderPosition.RIGHT, BorderPosition.VERTICAL, BorderPosition.LEFT);
            if (first && hasHeader){
                first = false;
                doRow(builder, columnWidths, BorderPosition.MIDDLE_LEFT, BorderPosition.HORIZONTAL, BorderPosition.MIDDLE_CENTER, BorderPosition.MIDDLE_RIGHT);
            }
        }
        doRow(builder, columnWidths, BorderPosition.BOTTOM_LEFT, BorderPosition.BOTTOM, BorderPosition.BOTTOM_CENTER, BorderPosition.BOTTOM_RIGHT);
        return builder.toString();
    }

    private void doRow(StringBuilder builder, MutableIntList columnWidths,
                       BorderPosition left, BorderPosition fill, BorderPosition center, BorderPosition right){
        builder.append(getChar(left));
        columnWidths.forEachWithIndex((width, index) -> {
            ColumnSetting setting = getSetting(index);
            width += setting.leftPadding + setting.rightPadding;
            builder.append(charArray(getChar(fill), width));
            builder.append(getChar(center));
        });
        builder.deleteCharAt(builder.length()-1);
        builder.append(getChar(right));
        builder.append('\n');
    }

    private void doRow(StringBuilder builder, MutableIntList columnWidths, MutableList<String> contents,
                       BorderPosition left, BorderPosition center, BorderPosition right){
        builder.append(getChar(left));
        columnWidths.forEachWithIndex((width, index) -> {
            ColumnSetting setting = getSetting(index);
            String content = "";
            if (contents.size() > index){
                content = contents.get(index);
            }
            builder.append(charArray(' ', setting.leftPadding));
            char[] remaining = charArray(' ', width-content.length());
            if (setting.leftAligned){
                builder.append(content);
                builder.append(remaining);
            } else {
                builder.append(remaining);
                builder.append(content);
            }
            builder.append(charArray(' ', setting.rightPadding));
            builder.append(getChar(center));
        });
        builder.deleteCharAt(builder.length()-1);
        builder.append(getChar(right));
        builder.append('\n');
    }

    private char[] charArray(char character, int count){
        char[] column = new char[count];
        Arrays.fill(column, character);
        return column;
    }

    private char getChar(BorderPosition position){
        switch (borderType){
            case NONE:
                return noneBorder(position);
            case ASCII:
            default:
                return asciiBorder(position);
        }
    }

    public <T> String display(MutableList<T> objects, Function<T, MutableList<String>> columns, MutableList<String> header){
        hasHeader = true;
        MutableList<MutableList<String>> table = Lists.mutable.empty();
        table.add(header);
        table.addAll(objects.collect(columns));
        return display(table);
    }

    public <T> String display(MutableList<T> objects, Function<T, MutableList<String>> columns){
        return display(objects.collect(columns));
    }

    public String display(String[][] table){
        MutableList<MutableList<String>> asList = Lists.mutable.empty();
        for (String[] row: table){
            asList.add(Lists.mutable.of(row));
        }
        return display(asList);
    }



    private class ColumnSetting{
        boolean leftAligned = true;
        int leftPadding = 1;
        int rightPadding = 1;

        protected ColumnSetting copy(){
            ColumnSetting clone = new ColumnSetting();
            clone.leftAligned = leftAligned;
            clone.leftPadding = leftPadding;
            clone.rightPadding = rightPadding;
            return clone;
        }
    }
}
