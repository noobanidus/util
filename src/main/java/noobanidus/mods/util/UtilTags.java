package noobanidus.mods.util;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class UtilTags {
  public static final Tags.IOptionalNamedTag<Item> VALUE1 = tag("value1");
  public static final Tags.IOptionalNamedTag<Item> VALUE2 = tag("value2");
  public static final Tags.IOptionalNamedTag<Item> VALUE3 = tag("value3");
  public static final Tags.IOptionalNamedTag<Item> VALUE4 = tag("value4");
  public static final Tags.IOptionalNamedTag<Item> VALUE5 = tag("value5");
  public static final Tags.IOptionalNamedTag<Item> VALUE6 = tag("value6");
  public static final Tags.IOptionalNamedTag<Item> VALUE7 = tag("value7");
  public static final Tags.IOptionalNamedTag<Item> VALUE8 = tag("value8");
  public static final Tags.IOptionalNamedTag<Item> VALUE9 = tag("value9");
  public static final Tags.IOptionalNamedTag<Item> VALUE10 = tag("value10");
  public static final Tags.IOptionalNamedTag<Item> VALUE15 = tag("value15");
  public static final Tags.IOptionalNamedTag<Item> VALUE20 = tag("value20");
  public static final Tags.IOptionalNamedTag<Item> VALUE25 = tag("value25");
  public static final Tags.IOptionalNamedTag<Item> VALUE50 = tag("value50");
  public static final Tags.IOptionalNamedTag<Item> VALUE75 = tag("value75");
  public static final Tags.IOptionalNamedTag<Item> VALUE100 = tag("value100");

  public static Tags.IOptionalNamedTag<Item> tag(String name) {
    return ItemTags.createOptional(new ResourceLocation(Util.MODID, name));
  }
}
