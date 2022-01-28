package com.mg.zeearchiver.data;

import java.util.ArrayList;
import java.util.List;

public class ArchiveItemsList {
    private List<ArchiveItem> items = new ArrayList<>();

    private void addItem(String itemPath, String itemDateTime, long itemUnPackedSize,
                         long itemPackedSize, boolean isFolder) {
        ArchiveItem item = new ArchiveItem(itemPath, itemDateTime, itemUnPackedSize,
                itemPackedSize, isFolder);
        items.add(item);
    }

    public List<ArchiveItem> getItems() {
        return items;
    }

    public static class ArchiveItem {
        public ArchiveItem(String itemPath, String itemDateTime, long itemUnPackedSize,
                           long itemPackedSize, boolean isFolder) {
            this.itemPath = itemPath;
            this.itemDateTime = itemDateTime;
            this.itemUnPackedSize = itemUnPackedSize;
            this.itemPackedSize = itemPackedSize;
            this.isFolder = isFolder;
        }

        public String getItemPath() {
            return itemPath;
        }

        public String getItemDateTime() {
            return itemDateTime;
        }

        public long getItemUnPackedSize() {
            return itemUnPackedSize;
        }

        public long getItemPackedSize() {
            return itemPackedSize;
        }

        public boolean isFolder() {
            return isFolder;
        }

        private String itemPath;
        private String itemDateTime;
        private long itemUnPackedSize;
        private long itemPackedSize;
        private boolean isFolder;
    }
}
