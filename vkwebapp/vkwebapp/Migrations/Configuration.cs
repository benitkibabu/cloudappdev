namespace vkwebapp.Migrations
{
    using Models;
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Migrations;
    using System.Linq;

    internal sealed class Configuration : DbMigrationsConfiguration<vkwebapp.Models.ServiceModels>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;
        }

        protected override void Seed(vkwebapp.Models.ServiceModels context)
        {
            //  This method will be called after migrating to the latest version.

            //  You can use the DbSet<T>.AddOrUpdate() helper extension method 
            //  to avoid creating duplicate seed data. E.g.
            //
            //    context.People.AddOrUpdate(
            //      p => p.FullName,
            //      new Person { FullName = "Andrew Peters" },
            //      new Person { FullName = "Brice Lambson" },
            //      new Person { FullName = "Rowan Miller" }
            //    );
            //
            //context.ClientUsers.AddOrUpdate(p => p.email,
            //    new ClientUser
            //    {
            //        id = 1,
            //        email = "d12side@hotmail.com",
            //        name = "benit kibabu",
            //        userid = "1231244231",
            //        imageurl = "empty",
            //        logintype = "local"
            //    });
        }
    }
}
