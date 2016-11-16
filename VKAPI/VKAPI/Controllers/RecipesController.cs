using System.Data.Entity;
using System.Data.Entity.Infrastructure;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.Http.Description;
using VKAPI.Models;

namespace VKAPI.Controllers
{
    public class RecipesController : ApiController
    {
        private virtualkitchendbEntities db = new virtualkitchendbEntities();

        // GET: api/recipes
        public async Task<IHttpActionResult> Getrecipes()
        {
            return Ok(await db.recipes.ToListAsync());
        }

        // GET: api/recipes/5
        [ResponseType(typeof(recipe))]
        public async Task<IHttpActionResult> Getrecipe(int id)
        {
            recipe recipe = await db.recipes.FindAsync(id);
            if (recipe == null)
            {
                return NotFound();
            }

            return Ok(recipe);
        }

        // PUT: api/recipes/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> Putrecipe(int id, [FromBody]recipe recipe)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != recipe.id)
            {
                return BadRequest();
            }

            db.Entry(recipe).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!recipeExists(id))
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

        // POST: api/recipes
        [ResponseType(typeof(recipe))]
        public async Task<IHttpActionResult> Postrecipe([FromBody]recipe recipe)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.recipes.Add(recipe);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = recipe.id }, recipe);
        }

        // DELETE: api/recipes/5
        [ResponseType(typeof(recipe))]
        public async Task<IHttpActionResult> Deleterecipe(int id)
        {
            recipe recipe = await db.recipes.FindAsync(id);
            if (recipe == null)
            {
                return NotFound();
            }

            db.recipes.Remove(recipe);
            await db.SaveChangesAsync();

            return Ok(recipe);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool recipeExists(int id)
        {
            return db.recipes.Count(e => e.id == id) > 0;
        }

        private bool recipeExists(string label)
        {
            return db.recipes.Count(e => e.label == label) > 0;
        }
    }
}