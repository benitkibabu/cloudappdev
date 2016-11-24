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
        }

        static VkitchenContext()
        {
            DbConfiguration.SetConfiguration(new MySql.Data.Entity.MySqlEFConfiguration());
        }

        public virtual DbSet<auth_app> auth_app { get; set; }
        public virtual DbSet<clientuser> clientusers { get; set; }
        public virtual DbSet<device> devices { get; set; }
        public virtual DbSet<ingredient> ingredients { get; set; }
        public virtual DbSet<recipe> recipes { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Entity<auth_app>()
                .Property(e => e.auth_key)
                .IsUnicode(false);

            modelBuilder.Entity<auth_app>()
                .Property(e => e.app_name)
                .IsUnicode(false);

            modelBuilder.Entity<clientuser>()
                .Property(e => e.logintype)
                .IsUnicode(false);

            modelBuilder.Entity<clientuser>()
                .Property(e => e.userid)
                .IsUnicode(false);

            modelBuilder.Entity<clientuser>()
                .Property(e => e.name)
                .IsUnicode(false);

            modelBuilder.Entity<clientuser>()
                .Property(e => e.email)
                .IsUnicode(false);

            modelBuilder.Entity<clientuser>()
                .Property(e => e.imageurl)
                .IsUnicode(false);

            modelBuilder.Entity<clientuser>()
                .HasMany(e => e.devices)
                .WithRequired(e => e.clientuser)
                .HasForeignKey(e => e.userid)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<clientuser>()
                .HasMany(e => e.ingredients)
                .WithRequired(e => e.clientuser)
                .HasForeignKey(e => e.userid)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<clientuser>()
                .HasMany(e => e.recipes)
                .WithRequired(e => e.clientuser)
                .HasForeignKey(e => e.userid)
                .WillCascadeOnDelete(false);

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
        }
    }
}
