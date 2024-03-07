package me.trouper.ultrals.data;

import io.github.itzispyder.pdk.utils.misc.JsonSerializable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeartItemStorage implements JsonSerializable<HeartItemStorage> {

    @Override
    public File getFile() {
        File file = new File("plugins/UltraLS/heart-item-storage.json");
        file.getParentFile().mkdirs();
        return file;
    }

    public Map<String,Integer> withdrawn = new HashMap<>();
}
