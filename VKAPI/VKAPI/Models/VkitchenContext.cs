namespace VKAPI.Models
{
    using System;
    using System.Data.Entity;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Linq;

    [DbConfigurationType(typeof(MySql.Data.Entity.MySqlEFConfiguration))]
    public partial class VkitchenContext : DbContext
    {
        public VkitchenContext()
            : base("name=VkitchenContext")
        {
            this.Configuration.ValidateOnSaveEnabled = false;
        }

        static VkitchenContext()
        {
            DbConfiguration.SetConfiguration(new MySql.Data.Entity.MySqlEFConfiguration());
        }

        public virtual DbSet<auth_app> auth_app { get; set; }
        public virtual DbSet<device> devices { get; set; }
        public virtual DbSet<ingredient> ingredients { get; set; }
        public virtual DbSet<my_recipes> my_recipes { get; set; }
        public virtual DbSet<recipe> recipes { get; set; }
        public virtual DbSet<user> users { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Entity<auth_app>()
                .Property(e => e.auth_key)
                .IsUnicode(false);

            modelBuilder.Entity<auth_app>()
                .Property(e => e.app_name)
                .IsUnicode(false);

            modelBuilder.Entity<device>()
                .Property(e => e.deviceid)
                .IsUnicode(false);

            modelBuilder.Entity<ingredient>()
                .Property(e => e.text)
                .IsUnicode(false);

            modelBuilder.Entity<ingredient>()
                .Property(e => e.measure)
                .IsUnicode(false);

            modelBuilder.Entity<ingredient>()
                .Property(e => e.food)
                .IsUnicode(false);

            modelBuilder.Entity<my_recipes>()
                .Property(e => e.uri)
                .IsUnicode(false);

            modelBuilder.Entity<my_recipes>()
                .Property(e => e.label)
                .IsUnicode(false);

            modelBuilder.Entity<my_recipes>()
                .Property(e => e.imageurl)
                .IsUnicode(false);

            modelBuilder.Entity<my_recipes>()
                .Property(e => e.source)
                .IsUnicode(false);

            modelBuilder.Entity<my_recipes>()
                .Property(e => e.url)
                .IsUnicode(false);

            modelBuilder.Entity<my_recipes>()
                .Property(e => e.shareas)
                .IsUnicode(false);

            modelBuilder.Entity<recipe>()
                .Property(e => e.uri)
                .IsUnicode(false);

            modelBuilder.Entity<recipe>()
                .Property(e => e.label)
                .IsUnicode(false);

            modelBuilder.Entity<recipe>()
                .Property(e => e.imageurl)
                .IsUnicode(false);

            modelBuilder.Entity<recipe>()
                .Property(e => e.source)
                .IsUnicode(false);

            modelBuilder.Entity<recipe>()
                .Property(e => e.url)
                .IsUnicode(false);

            modelBuilder.Entity<recipe>()
                .Property(e => e.shareas)
                .IsUnicode(false);

            modelBuilder.Entity<recipe>()
                .Property(e => e.dietlabel)
                .IsUnicode(false);

            modelBuilder.Entity<recipe>()
                .Property(e => e.healthlabel)
                .IsUnicode(false);

            modelBuilder.Entity<recipe>()
                .Property(e => e.caution)
                .IsUnicode(false);

            modelBuilder.Entity<recipe>()
                .Property(e => e.ingredientlines)
                .IsUnicode(false);

            modelBuilder.Entity<user>()
                .Property(e => e.logintype)
                .IsUnicode(false);

            modelBuilder.Entity<user>()
                .Property(e => e.userid)
                .IsUnicode(false);

            modelBuilder.Entity<user>()
                .Property(e => e.name)
                .IsUnicode(false);

            modelBuilder.Entity<user>()
                .Property(e => e.email)
                .IsUnicode(false);

            modelBuilder.Entity<user>()
                .Property(e => e.imageurl)
                .IsUnicode(false);

            modelBuilder.Entity<user>()
                .HasMany(e => e.devices)
                .WithRequired(e => e.user)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<user>()
                .HasMany(e => e.ingredients)
                .WithRequired(e => e.user)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<user>()
                .HasMany(e => e.my_recipes)
                .WithRequired(e => e.user)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<user>()
                .HasMany(e => e.recipes)
                .WithRequired(e => e.user)
                .WillCascadeOnDelete(false);
        }
    }
}
