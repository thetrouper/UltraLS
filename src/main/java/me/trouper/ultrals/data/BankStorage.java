package me.trouper.ultrals.data;

import io.github.itzispyder.pdk.utils.misc.JsonSerializable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BankStorage implements JsonSerializable<BankStorage> {

    @Override
    public File getFile() {
        File file = new File("plugins/UltraLS/storage/banks.json");
        file.getParentFile().mkdirs();
        return file;
    }

    public Map<String,Integer> balances = new HashMap<>();
}
