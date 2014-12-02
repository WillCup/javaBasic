package com.will.tooljars.apache.commons.chain;
import org.apache.commons.chain.Catalog;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.apache.commons.chain.config.ConfigParser;
import org.apache.commons.chain.impl.CatalogFactoryBase;

/**
 * 使用Commons Digester来读取和解析配置文件
 * 
 * @author Will
 * @created at 2014-8-29 下午1:36:36
 */
public class CatalogLoader {
    private static final String CONFIG_FILE = 
        "/com/jadecove/chain/sample/catalog.xml";
    private ConfigParser parser;
    private Catalog catalog;
    
    public CatalogLoader() {
        parser = new ConfigParser();
    }
    public Catalog getCatalog() throws Exception {
        if (catalog == null) {
        
    parser.parse(this.getClass().getResource(CONFIG_FILE));        
    
        }
        catalog = CatalogFactoryBase.getInstance().getCatalog();
        return catalog;
    }
    public static void main(String[] args) throws Exception {
        CatalogLoader loader = new CatalogLoader();
        Catalog sampleCatalog = loader.getCatalog();
        Command command = sampleCatalog.getCommand("sell-vehicle");
        Context ctx = new SellVehicleContext();
        command.execute(ctx);
    }
}