package com.geocube.lists.maksList;

import ru.spb.osll.objects.Mark;

public class MarkItem {
    private Mark mark;
    public boolean isSelected;

    public MarkItem(Mark mark) {
        this.mark = mark;
        isSelected = false;
    }

    public Mark getMark() {
        return mark;
    }
}
