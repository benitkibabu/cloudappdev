namespace VKAPI.Migrations
{
    using Models;
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Migrations;
    using System.Linq;

    internal sealed class Configuration : DbMigrationsConfiguration<VKAPI.Models.VkitchenContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;

            SetSqlGenerator("MySql.Data.MySqlClient", new MySql.Data.Entity.MySqlMigrationSqlGenerator());
        }

        protected override void Seed(VKAPI.Models.VkitchenContext context)
        {
            //  This method will be called after migrating to the latest version.
            
            context.auth_app.AddOrUpdate(
                app => app.app_name,
                new auth_app {
                app_name = "Virtual Kictchen",
                auth_key =  Guid.NewGuid().ToString() });

            context.SaveChangesAsync();
        }
    }
}
