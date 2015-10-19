package kalara.tree.oil;

/**
 * Created by avigma19 on 10/9/2015.
 */
public class Knowlegde_item {
    String id;
    String report;
    String barcode;
    String product;
    String size;
    String Productimage;
    String status;
    String productcategory;
    String productname;
    String productvideo;
    String time;
int position;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public Knowlegde_item(int position,


        String Productimage,String id
        ){


   this.id=id;


/*    this.barcode=barcode;
   // this.product=product;
    this.size=si*/
    this.Productimage=Productimage;
        this.position=position;
    /*this.status=status;
    this.productcategory=productcategory;
    this. productname=productname;
    this.productvideo=productvideo;
    this.time=time;*/

}



    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getProductimage() {
        return Productimage;
    }

    public void setProductimage(String productimage) {
        Productimage = productimage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProductcategory() {
        return productcategory;
    }

    public void setProductcategory(String productcategory) {
        this.productcategory = productcategory;
    }

    public String getProductvideo() {
        return productvideo;
    }

    public void setProductvideo(String productvideo) {
        this.productvideo = productvideo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
