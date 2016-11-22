namespace VKAPI.Models
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("virtualkitchendb.auth_app")]
    public partial class auth_app
    {
        public int id { get; set; }

        [Required]
        [StringLength(200)]
        public string auth_key { get; set; }

        [Required]
        [StringLength(200)]
        public string app_name { get; set; }
    }
}
