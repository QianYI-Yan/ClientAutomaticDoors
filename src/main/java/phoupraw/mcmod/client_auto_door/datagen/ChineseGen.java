package phoupraw.mcmod.client_auto_door.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;
import phoupraw.mcmod.client_auto_door.ClientAutomaticDoors;

import java.util.concurrent.CompletableFuture;

import static phoupraw.mcmod.client_auto_door.ClientAutomaticDoors.ID;

final class ChineseGen extends FabricLanguageProvider {
    ChineseGen(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(dataOutput, "zh_cn", registryLookup);
    }
    @Override
    public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder b) {
        String modName = "客户端自动门";
        b.add(ClientAutomaticDoors.NAME_KEY, modName);
        b.add("modmenu.summaryTranslation." + ID, "自动开关门");
        b.add("modmenu.descriptionTranslation." + ID, """
          自动开关门、活板门、栅栏门。 
          执行命令`/trilevel_config global client_auto_door.json set on 0`以关闭此模组；
          执行命令`/trilevel_config global client_auto_door.json set on 1`以开启此模组。
          """);
    }
}
