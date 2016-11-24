namespace VKAPI.Models
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("virtualkitchendb.ingredients")]
    public partial class ingredient
    {
        public int id { get; set; }

        public int userid { get; set; }

        [Required]
        [StringLength(200)]
        public string text { get; set; }

        public decimal quantity { get; set; }

        [Required]
        [StringLength(100)]
        public string measure { get; set; }

        public decimal weight { get; set; }

        [Required]
        [StringLength(100)]
        public string food { get; set; }

        public virtual clientuser clientuser { get; set; }
    }
}
