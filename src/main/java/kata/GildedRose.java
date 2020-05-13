package kata;


import java.util.Arrays;
import java.util.HashMap;
import java.util.function.UnaryOperator;

class GildedRose {
    public static final String BACKSTAGE = "Backstage passes to a TAFKAL80ETC concert";
    public static final String SULFURAS = "Sulfuras, Hand of Ragnaros";
    public static final String BRIE = "Aged Brie";
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    UnaryOperator<Item> sulfurasOp = (item -> item);

    UnaryOperator<Item> basic = (Item i) -> {
        i.sellIn--;
        i.quality = Math.max(i.sellIn > 0 ? i.quality - 1 : i.quality - 2, 0);
        return i;
    };

    UnaryOperator<Item> brieOp = (Item i) -> {
        i.sellIn--;
        i.quality = Math.min(++i.quality, 50);
        return i;
    };

    UnaryOperator<Item> backstageOp = (Item i) -> {
        i.sellIn--;
        i.quality++;
        if (i.sellIn <= 10)
            i.quality++;
        if (i.sellIn <= 5)
            i.quality++;
        if (i.sellIn < 0)
            i.quality = 0;
        if (i.quality > 50)
            i.quality = 50;
        return i;
    };

    UnaryOperator<Item> conjuredOp = (Item i) -> {
        i.sellIn--;
        i.quality = Math.max(i.sellIn > 0 ? i.quality - 2 : i.quality - 4, 0);
        return i;
    };


    public void updateQualityRefactored() {
        HashMap<String, UnaryOperator<Item>> updateLogic = new HashMap<>();
        updateLogic.put("conjured", conjuredOp);
        updateLogic.put(BACKSTAGE, backstageOp);
        updateLogic.put("basic", basic);
        updateLogic.put(SULFURAS, sulfurasOp);
        updateLogic.put(BRIE, brieOp);
        items = Arrays.stream(this.items)
                .map(item -> updateLogic.getOrDefault(item.name.contains("Conjured") ? "conjured" : item.name, basic).apply(item))
                .toArray(Item[]::new);
    }


    public void updateQuality() {

        for (int i = 0; i < this.items.length; i++) {
            if (!this.items[i].name.equals(BRIE) && !this.items[i].name.equals(BACKSTAGE)) {
                if ((this.items[i].quality > 0) && (!this.items[i].name.equals(SULFURAS))) {
                    this.items[i].quality = this.items[i].quality - 1;
                }
            } else {
                if (this.items[i].quality < 50) {
                    this.items[i].quality = this.items[i].quality + 1;

                    if (this.items[i].name.equals(BACKSTAGE)) {
                        if ((this.items[i].sellIn < 11) && (this.items[i].quality < 50)) {
                            this.items[i].quality = this.items[i].quality + 1;
                        }

                        if ((this.items[i].sellIn < 6) && (this.items[i].quality < 50)) {
                            this.items[i].quality++;
                        }
                    }
                }
            }

            if (!this.items[i].name.equals(SULFURAS)) {
                this.items[i].sellIn = this.items[i].sellIn - 1;
            }

            if (this.items[i].sellIn < 0) {
                if (!this.items[i].name.equals(BRIE)) {
                    if ((!this.items[i].name.equals(BACKSTAGE)) && (this.items[i].quality > 0 && !this.items[i].name.equals(SULFURAS))) {
                        this.items[i].quality = this.items[i].quality - 1;
                    }
                } else {
                    this.items[i].quality = 0;
                }
            } else {
                if (this.items[i].quality < 50) {
                    this.items[i].quality = this.items[i].quality + 1;
                }
            }
        }
    }
}
