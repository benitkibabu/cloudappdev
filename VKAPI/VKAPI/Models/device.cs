namespace VKAPI.Models
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("virtualkitchendb.devices")]
    public partial class device
    {
        public int id { get; set; }

        public int userid { get; set; }

        [Required]
        [StringLength(500)]
        public string deviceid { get; set; }

        public virtual clientuser clientuser { get; set; }
    }
}
