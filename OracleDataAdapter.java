
public class OracleDataAdapter implements IDataAdapter {
    public int connect(String dbfile) {
        //...
        return CONNECTION_OPEN_OK;
    }

    public int disconnect() {
        // ...
        return CONNECTION_CLOSE_OK;

    }

    public ProductModel loadProduct(int id) {
        return null;
    }
    public int saveProduct(ProductModel model) {
        return PRODUCT_SAVE_OK;
    }
    public ProductListModel searchProduct(String name, double minPrice, double maxPrice) { return null; }
    public CustomerModel loadCustomer(int id) {
        return null;
    }
    public int saveCustomer(CustomerModel model) {
        return CUSTOMER_SAVE_OK;
    }
    public PurchaseModel loadPurchase(int id) {
        return null;
    }
    public int savePurchase(PurchaseModel model) {
        return PURCHASE_SAVE_OK;
    }
    public UserModel loadUser(String username)
    {
        return null;
    }
    public int saveUser(UserModel user) { return USER_SAVE_OK; };
    public PurchaseListModel loadPurchaseHistory(int id, boolean managerView) { return null; }
}