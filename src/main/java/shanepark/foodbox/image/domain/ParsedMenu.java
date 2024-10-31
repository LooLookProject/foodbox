package shanepark.foodbox.image.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ParsedMenu {
    private final String date;
    private final List<String> menus = new ArrayList<>();

    public ParsedMenu(String date) {
        this.date = date.replaceAll("\n", "");
    }

    public void setMenu(String menu) {
        for (String m : menu.split("\n")) {
            m = m.trim();
            if (m.isEmpty())
                continue;
            menus.add(m);
        }
    }

    @Override
    public String toString() {
        return String.format("<%s>\n%s\n", date, menus.toString());
    }
}
