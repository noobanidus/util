package noobanidus.mods.util;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeTagHandler;
import net.minecraftforge.registries.ForgeRegistries;

public class UtilTags {
  public static final ITag.INamedTag<Item> VALUE1 = tag("value1");
  public static final ITag.INamedTag<Item> VALUE2 = tag("value2");
  public static final ITag.INamedTag<Item> VALUE3 = tag("value3");
  public static final ITag.INamedTag<Item> VALUE4 = tag("value4");
  public static final ITag.INamedTag<Item> VALUE5 = tag("value5");
  public static final ITag.INamedTag<Item> VALUE6 = tag("value6");
  public static final ITag.INamedTag<Item> VALUE7 = tag("value7");
  public static final ITag.INamedTag<Item> VALUE8 = tag("value8");
  public static final ITag.INamedTag<Item> VALUE9 = tag("value9");
  public static final ITag.INamedTag<Item> VALUE10 = tag("value10");
  public static final ITag.INamedTag<Item> VALUE15 = tag("value15");
  public static final ITag.INamedTag<Item> VALUE20 = tag("value20");
  public static final ITag.INamedTag<Item> VALUE25 = tag("value25");
  public static final ITag.INamedTag<Item> VALUE50 = tag("value50");
  public static final ITag.INamedTag<Item> VALUE75 = tag("value75");
  public static final ITag.INamedTag<Item> VALUE100 = tag("value100");

  public static ITag.INamedTag<Item> tag(String name) {
    return ForgeTagHandler.makeWrapperTag(ForgeRegistries.ITEMS, new ResourceLocation(Util.MODID, name));
    //return ItemTags.createOptional(new ResourceLocation(Util.MODID, name));
  }
}
