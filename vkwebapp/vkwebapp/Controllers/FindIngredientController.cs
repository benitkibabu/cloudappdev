using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using vkwebapp.Models;

namespace vkwebapp.Controllers
{
    public class FindIngredientController : ApiController
    {
        private ServiceModels db = new ServiceModels();

        // GET: api/FindIngredient/5
        [ResponseType(typeof(Ingredient))]
        public async Task<IHttpActionResult> GetIngredient([FromBody]SearchIngredient search)
        {
            HttpRequestHeaders headers = this.Request.Headers;
            string app_key = string.Empty;
            if (headers.Contains("app_key"))
            {
                app_key = headers.GetValues("app_key").First();
            }

            AuthApp app = await db.AuthApps.Where(a => a.auth_key.Equals(app_key)).FirstAsync();
            if (app == null)
            {
                return NotFound();
            }

            List<Ingredient> ingredients = db.Ingredients.ToList();
            List<Ingredient> foundIngredients = new List<Ingredient>();
            foreach (Ingredient i in ingredients)
            {
                foreach(String words in search.searchWords)
                {
                    if (i.text.Contains(words))
                    {
                        foundIngredients.Add(i);
                    }
                }
               
            }
            //if (ingredient == null)
            //{
            //    return NotFound();
            //}

            return Ok(foundIngredients);
        }


        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool IngredientExists(int id)
        {
            return db.Ingredients.Count(e => e.id == id) > 0;
        }
    }
}