using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace vkwebapp.Models
{
    public class ServiceModels : DbContext
    {
        public ServiceModels() : base("DefaultConnection")
        {

        }

        public DbSet<ClientUser> ClientUsers { get; set; }
        public DbSet<Recipe> Recipes { get; set; }
        public DbSet<Device> Devices { get; set; }
        public DbSet<Ingredient> Ingredients { get; set; }
        public DbSet<AuthApp> AuthApps { get; set; }

    }

    public class ClientUser
    {
        public ClientUser()
        {
            devices = new HashSet<Device>();
            ingredients = new HashSet<Ingredient>();
            recipes = new HashSet<Recipe>();
        }

        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int id { get; set; }

        [Required]
        [StringLength(50)]
        [Display(Name ="Logged In From")]
        public string logintype { get; set; }

        [Required]
        [StringLength(500)]
        [Display(Name ="Source UID")]
        public string userid { get; set; }

        [Required]
        [StringLength(100)]
        public string name { get; set; }

        [Required]
        [StringLength(100)]
        [EmailAddress]
        public string email { get; set; }

        [Required]
        [StringLength(500)]
        public string imageurl { get; set; }

        public virtual ICollection<Device> devices { get; set; }

        public virtual ICollection<Ingredient> ingredients { get; set; }

        public virtual ICollection<Recipe> recipes { get; set; }
    }

    public class Ingredient
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int id { get; set; }

        [Display(Name = "User Id")]
        public int userid { get; set; }

        [Required]
        [StringLength(200)]
        public string text { get; set; }

        public decimal quantity { get; set; }

        [ForeignKey("userid")]
        public virtual ClientUser clientuser { get; set; }
    }

    public class Device
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int id { get; set; }

        [Display(Name ="User")]
        public int userid { get; set; }

        [Required]
        [StringLength(500)]
        [Display(Name ="Device ID")]
        public string deviceid { get; set; }

        [ForeignKey("userid")]
        public virtual ClientUser clientuser { get; set; }
    }

    public class Recipe
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int id { get; set; }

        [StringLength(750)]
        public string uri { get; set; }

        [StringLength(750)]
        public string label { get; set; }

        [StringLength(750)]
        public string imageurl { get; set; }

        [StringLength(750)]
        public string source { get; set; }

        [StringLength(750)]
        public string url { get; set; }

        [StringLength(750)]
        public string shareas { get; set; }

        public double yield { get; set; }

        [StringLength(750)]
        public string dietlabel { get; set; }

        [StringLength(750)]
        public string healthlabel { get; set; }

        [StringLength(750)]
        public string caution { get; set; }

        [StringLength(750)]
        public string ingredientlines { get; set; }

        public double calories { get; set; }

        public double totalweight { get; set; }

        [Display(Name ="User")]
        public int userid { get; set; }

        [ForeignKey("userid")]
        public virtual ClientUser clientuser { get; set; }
    }

    public class AuthApp
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int id { get; set; }

        [Required]
        [StringLength(200)]
        [Display(Name ="Authorisation Key")]
        public string auth_key { get; set; }

        [Required]
        [StringLength(200)]
        [Display(Name ="Application Name")]
        public string app_name { get; set; }

        [Required]
        [StringLength(128)]
        [Display(Name = "User Name")]
        public string UserID { get; set; }
    }
}