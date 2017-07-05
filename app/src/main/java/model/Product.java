package model;

/**
 * Created by skyli on 02/07/2017.
 */

public class Product {
    private String sellerToken;
    private String productToken;
    private int    imgPrincipal;
    private String productTitle;
    private String productDescript;
    private String productCategory;
    private int    productQtde;
    private int    productStatus;
    private double productPrice;

    public int getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(int productStatus) {
        this.productStatus = productStatus;
    }

    public String getSellerToken() {
        return sellerToken;
    }

    public void setSellerToken(String sellerToken) {
        this.sellerToken = sellerToken;
    }

    public String getProductToken() {
        return productToken;
    }

    public void setProductToken(String productToken) {
        this.productToken = productToken;
    }

    public int getImgPrincipal() {
        return imgPrincipal;
    }

    public void setImgPrincipal(int imgPrincipal) {
        this.imgPrincipal = imgPrincipal;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescript() {
        return productDescript;
    }

    public void setProductDescript(String productDescript) {
        this.productDescript = productDescript;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public int getProductQtde() {
        return productQtde;
    }

    public void setProductQtde(int productQtde) {
        this.productQtde = productQtde;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }
}
