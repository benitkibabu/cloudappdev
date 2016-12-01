namespace vkwebapp.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class InitialCreate : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.AuthApps",
                c => new
                    {
                        id = c.Int(nullable: false, identity: true),
                        auth_key = c.String(nullable: false, maxLength: 200),
                        app_name = c.String(nullable: false, maxLength: 200),
                        UserID = c.String(nullable: false, maxLength: 128),
                    })
                .PrimaryKey(t => t.id);
            
            CreateTable(
                "dbo.ClientUsers",
                c => new
                    {
                        id = c.Int(nullable: false, identity: true),
                        logintype = c.String(nullable: false, maxLength: 50),
                        userid = c.String(nullable: false, maxLength: 500),
                        name = c.String(nullable: false, maxLength: 100),
                        email = c.String(nullable: false, maxLength: 100),
                        imageurl = c.String(nullable: false, maxLength: 500),
                    })
                .PrimaryKey(t => t.id);
            
            CreateTable(
                "dbo.Devices",
                c => new
                    {
                        id = c.Int(nullable: false, identity: true),
                        userid = c.Int(nullable: false),
                        deviceid = c.String(nullable: false, maxLength: 500),
                    })
                .PrimaryKey(t => t.id)
                .ForeignKey("dbo.ClientUsers", t => t.userid, cascadeDelete: true)
                .Index(t => t.userid);
            
            CreateTable(
                "dbo.Ingredients",
                c => new
                    {
                        id = c.Int(nullable: false, identity: true),
                        userid = c.Int(nullable: false),
                        text = c.String(nullable: false, maxLength: 200),
                        quantity = c.Decimal(nullable: false, precision: 18, scale: 2),
                    })
                .PrimaryKey(t => t.id)
                .ForeignKey("dbo.ClientUsers", t => t.userid, cascadeDelete: true)
                .Index(t => t.userid);
            
            CreateTable(
                "dbo.Recipes",
                c => new
                    {
                        id = c.Int(nullable: false, identity: true),
                        uri = c.String(maxLength: 750),
                        label = c.String(maxLength: 750),
                        imageurl = c.String(maxLength: 750),
                        source = c.String(maxLength: 750),
                        url = c.String(maxLength: 750),
                        shareas = c.String(maxLength: 750),
                        yield = c.Double(nullable: false),
                        dietlabel = c.String(maxLength: 750),
                        healthlabel = c.String(maxLength: 750),
                        caution = c.String(maxLength: 750),
                        ingredientlines = c.String(maxLength: 750),
                        calories = c.Double(nullable: false),
                        totalweight = c.Double(nullable: false),
                        userid = c.Int(nullable: false),
                    })
                .PrimaryKey(t => t.id)
                .ForeignKey("dbo.ClientUsers", t => t.userid, cascadeDelete: true)
                .Index(t => t.userid);
            
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Recipes", "userid", "dbo.ClientUsers");
            DropForeignKey("dbo.Ingredients", "userid", "dbo.ClientUsers");
            DropForeignKey("dbo.Devices", "userid", "dbo.ClientUsers");
            DropIndex("dbo.Recipes", new[] { "userid" });
            DropIndex("dbo.Ingredients", new[] { "userid" });
            DropIndex("dbo.Devices", new[] { "userid" });
            DropTable("dbo.Recipes");
            DropTable("dbo.Ingredients");
            DropTable("dbo.Devices");
            DropTable("dbo.ClientUsers");
            DropTable("dbo.AuthApps");
        }
    }
}
