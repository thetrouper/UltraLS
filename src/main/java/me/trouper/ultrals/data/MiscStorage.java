package me.trouper.ultrals.data;

import io.github.itzispyder.pdk.utils.misc.JsonSerializable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiscStorage implements JsonSerializable<MiscStorage> {

    @Override
    public File getFile() {
        File file = new File("plugins/UltraLS/storage/misc-storage.json");
        file.getParentFile().mkdirs();
        return file;
    }

    public List<String> deadPlayers = new ArrayList<>();
}
