package kalara.tree.oil;

/**
 * Created by avigma19 on 10/1/2015.
 */
public class NavDrawerItem {

    private String title;
    private String desc;
    private int icon;
    private int backicon;

    public NavDrawerItem() {

    }

    public NavDrawerItem( String title,String desc,int icon,int backicon) {

        this.title = title;
        this.desc=desc;
        this.icon=icon;
        this.backicon=backicon;
    }

    public String getDesc() {
        return desc;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setDesc(String desc) {
        this.desc = desc;

    }

    public int getBackicon() {
        return backicon;
    }

    public void setBackicon(int backicon) {
        this.backicon = backicon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
