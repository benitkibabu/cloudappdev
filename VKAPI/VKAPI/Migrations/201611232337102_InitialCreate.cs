namespace VKAPI.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class InitialCreate : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "virtualkitchendb.auth_app",
                c => new
                    {
                        id = c.Int(nullable: false, identity: true),
                        auth_key = c.String(nullable: false, maxLength: 200, unicode: false),
                        app_name = c.String(nullable: false, maxLength: 200, unicode: false),
                    })
                .PrimaryKey(t => t.id);
            
            CreateTable(
                "virtualkitchendb.clientuser",
                c => new
                    {
                        id = c.Int(nullable: false, identity: true),
                        logintype = c.String(nullable: false, maxLength: 50, unicode: false),
                        userid = c.String(nullable: false, maxLength: 500, unicode: false),
                        name = c.String(nullable: false, maxLength: 100, unicode: false),
                        email = c.String(nullable: false, maxLength: 100, unicode: false),
                        imageurl = c.String(nullable: false, maxLength: 500, unicode: false),
                    })
                .PrimaryKey(t => t.id);
            
            CreateTable(
                "virtualkitchendb.devices",
                c => new
                    {
                        id = c.Int(nullable: false, identity: true),
                        userid = c.Int(nullable: false),
                        deviceid = c.String(nullable: false, maxLength: 500, unicode: false),
                    })
                .PrimaryKey(t => t.id)
                .ForeignKey("virtualkitchendb.clientuser", t => t.userid)
                .Index(t => t.userid);
            
            CreateTable(
                "virtualkitchendb.ingredients",
                c => new
                    {
                        id = c.Int(nullable: false, identity: true),
                        userid = c.Int(nullable: false),
                        text = c.String(nullable: false, maxLength: 200, unicode: false),
                        quantity = c.Decimal(nullable: false, precision: 18, scale: 2),
                        measure = c.String(nullable: false, maxLength: 100, unicode: false),
                        weight = c.Decimal(nullable: false, precision: 18, scale: 2),
                        food = c.String(nullable: false, maxLength: 100, unicode: false),
                    })
                .PrimaryKey(t => t.id)
                .ForeignKey("virtualkitchendb.clientuser", t => t.userid)
                .Index(t => t.userid);
            
            CreateTable(
                "virtualkitchendb.recipes",
                c => new
                    {
                        id = c.Int(nullable: false, identity: true),
                        uri = c.String(unicode: false),
                        label = c.String(unicode: false),
                        imageurl = c.String(unicode: false),
                        source = c.String(unicode: false),
                        url = c.String(unicode: false),
                        shareas = c.String(unicode: false),
                        yield = c.Decimal(precision: 18, scale: 2),
                        dietlabel = c.String(unicode: false),
                        healthlabel = c.String(unicode: false),
                        caution = c.String(unicode: false),
                        ingredientlines = c.String(unicode: false),
                        calories = c.Decimal(precision: 18, scale: 2),
                        totalweight = c.Decimal(precision: 18, scale: 2),
                        userid = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.id)
                .ForeignKey("virtualkitchendb.clientuser", t => t.userid)
                .Index(t => t.userid);
            
        }
        
        public override void Down()
        {
            DropForeignKey("virtualkitchendb.recipes", "userid", "virtualkitchendb.clientuser");
            DropForeignKey("virtualkitchendb.ingredients", "userid", "virtualkitchendb.clientuser");
            DropForeignKey("virtualkitchendb.devices", "userid", "virtualkitchendb.clientuser");
            DropIndex("virtualkitchendb.recipes", new[] { "userid" });
            DropIndex("virtualkitchendb.ingredients", new[] { "userid" });
            DropIndex("virtualkitchendb.devices", new[] { "userid" });
            DropTable("virtualkitchendb.recipes");
            DropTable("virtualkitchendb.ingredients");
            DropTable("virtualkitchendb.devices");
            DropTable("virtualkitchendb.clientuser");
            DropTable("virtualkitchendb.auth_app");
        }
    }
}
