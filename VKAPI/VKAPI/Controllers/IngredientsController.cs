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
    public class IngredientsController : ApiController
    {
        private VkitchenContext db = new VkitchenContext();

        // GET: api/Ingredients
        public async Task<IHttpActionResult> Getingredients()
        {
            return Ok( await db.ingredients.ToListAsync());
        }

        // GET: api/Ingredients/5
        [ResponseType(typeof(ingredient))]
        public async Task<IHttpActionResult> Getingredient(string auth_key, int id)
        {
            ingredient ingredient = await db.ingredients.FindAsync(id);
            if (ingredient == null)
            {
                return NotFound();
            }

            return Ok(ingredient);
        }

        // PUT: api/Ingredients/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> Putingredient(string auth_key,int id, ingredient ingredient)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != ingredient.id)
            {
                return BadRequest();
            }

            db.Entry(ingredient).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ingredientExists(id))
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

        // POST: api/Ingredients
        [ResponseType(typeof(ingredient))]
        public async Task<IHttpActionResult> Postingredient(string auth_key, ingredient ingredient)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.ingredients.Add(ingredient);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = ingredient.id }, ingredient);
        }

        // DELETE: api/Ingredients/5
        [ResponseType(typeof(ingredient))]
        public async Task<IHttpActionResult> Deleteingredient(string auth_key, int id)
        {
            ingredient ingredient = await db.ingredients.FindAsync(id);
            if (ingredient == null)
            {
                return NotFound();
            }

            db.ingredients.Remove(ingredient);
            await db.SaveChangesAsync();

            return Ok(ingredient);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool ingredientExists(int id)
        {
            return db.ingredients.Count(e => e.id == id) > 0;
        }
    }
}