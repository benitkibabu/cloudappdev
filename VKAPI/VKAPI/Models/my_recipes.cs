namespace VKAPI.Models
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("virtualkitchendb.my_recipes")]
    public partial class my_recipes
    {
        public int id { get; set; }

        [StringLength(1073741823)]
        public string uri { get; set; }

        [StringLength(1073741823)]
        public string label { get; set; }

        [StringLength(1073741823)]
        public string imageurl { get; set; }

        [StringLength(1073741823)]
        public string source { get; set; }

        [StringLength(1073741823)]
        public string url { get; set; }

        [StringLength(1073741823)]
        public string shareas { get; set; }

        public decimal? yield { get; set; }

        public decimal? calories { get; set; }

        public decimal? totalweight { get; set; }

        public int userid { get; set; }

        public virtual user user { get; set; }
    }
}
