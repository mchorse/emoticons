package mchorse.emoticons.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import mchorse.emoticons.client.gui.GuiEmotesList.EmoteEntry;
import mchorse.emoticons.common.emotes.Emotes;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiSearchListElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiEmotesList extends GuiListElement<EmoteEntry>
{
    public GuiEmotesList(Minecraft mc, Consumer<List<EmoteEntry>> callback)
    {
        super(mc, callback);

        this.background();
    }

    @Override
    protected boolean sortElements()
    {
        Collections.sort(this.list, (o1, o2) -> o1.title.get().compareToIgnoreCase(o2.title.get()));

        return true;
    }

    @Override
    protected String elementToString(EmoteEntry element)
    {
        return element.title.get();
    }

    public void setCurrent(String element)
    {
        for (int i = 0, c = this.list.size(); i < c; i++)
        {
            if (this.list.get(i).key.equals(element))
            {
                this.setIndex(i);

                break;
            }
        }
    }

    public static class EmoteEntry
    {
        public String key;
        public IKey title;

        public EmoteEntry(String key)
        {
            this.key = key;
            this.title = IKey.str(Emotes.getDefault(key).getTitle());
        }

        @Override
        public String toString()
        {
            return this.title.get();
        }
    }

    public static class GuiSeachEmoteList extends GuiSearchListElement<EmoteEntry>
    {
        public Map<String, EmoteEntry> emotes = new HashMap<String, EmoteEntry>();

        public GuiSeachEmoteList(Minecraft mc, Consumer<List<EmoteEntry>> callback)
        {
            super(mc, callback);

            List<EmoteEntry> emotes = new ArrayList<EmoteEntry>();

            for (String key : Emotes.EMOTES.keySet())
            {
                EmoteEntry entry = new EmoteEntry(key);

                emotes.add(entry);
                this.emotes.put(key, entry);
            }

            this.list.add(emotes);
            this.list.sort();

            this.filter("", false);
        }

        @Override
        protected GuiListElement<EmoteEntry> createList(Minecraft mc, Consumer<List<EmoteEntry>> callback)
        {
            return new GuiEmotesList(mc, callback);
        }
    }
}