using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using VKAPI.Models;

namespace VKAPI.Controllers
{
    public class MyRecipesController : ApiController
    {
        private virtualkitchendbEntities db = new virtualkitchendbEntities();

        // GET: api/MyRecipes
        public IQueryable<my_recipes> Getmy_recipes()
        {
            return db.my_recipes;
        }

        // GET: api/MyRecipes/5
        [ResponseType(typeof(my_recipes))]
        public async Task<IHttpActionResult> Getmy_recipes(int id)
        {
            my_recipes my_recipes = await db.my_recipes.FindAsync(id);
            if (my_recipes == null)
            {
                return NotFound();
            }

            return Ok(my_recipes);
        }

        // PUT: api/MyRecipes/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> Putmy_recipes(int id, my_recipes my_recipes)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != my_recipes.id)
            {
                return BadRequest();
            }

            db.Entry(my_recipes).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!my_recipesExists(id))
                {
                    return NotFound();
                }
                else
                {
                    throw;
                }
            }

            return StatusCode(HttpStatusCode.NoContent);
        }

        // POST: api/MyRecipes
        [ResponseType(typeof(my_recipes))]
        public async Task<IHttpActionResult> Postmy_recipes(my_recipes my_recipes)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.my_recipes.Add(my_recipes);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = my_recipes.id }, my_recipes);
        }

        // DELETE: api/MyRecipes/5
        [ResponseType(typeof(my_recipes))]
        public async Task<IHttpActionResult> Deletemy_recipes(int id)
        {
            my_recipes my_recipes = await db.my_recipes.FindAsync(id);
            if (my_recipes == null)
            {
                return NotFound();
            }

            db.my_recipes.Remove(my_recipes);
            await db.SaveChangesAsync();

            return Ok(my_recipes);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool my_recipesExists(int id)
        {
            return db.my_recipes.Count(e => e.id == id) > 0;
        }
    }
}