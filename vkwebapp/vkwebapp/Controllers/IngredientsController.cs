﻿using System;
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
    public class IngredientsController : ApiController
    {
        private ServiceModels db = new ServiceModels();

        // GET: api/Ingredients
        public async Task<IHttpActionResult> GetIngredients()
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
            return Ok (await db.Ingredients.ToListAsync());
        }

        // GET: api/Ingredients/5
        [ResponseType(typeof(Ingredient))]
        public async Task<IHttpActionResult> GetIngredient(int id)
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

            Ingredient ingredient = await db.Ingredients.FindAsync(id);
            if (ingredient == null)
            {
                return NotFound();
            }

            return Ok(ingredient);
        }

        // PUT: api/Ingredients/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutIngredient([FromBody]int id, [FromBody]Ingredient ingredient)
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
                if (!IngredientExists(id))
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
        [ResponseType(typeof(Ingredient))]
        public async Task<IHttpActionResult> PostIngredient( [FromBody]Ingredient ingredient)
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

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.Ingredients.Add(ingredient);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = ingredient.id }, ingredient);
        }

        // DELETE: api/Ingredients/5
        [ResponseType(typeof(Ingredient))]
        public async Task<IHttpActionResult> DeleteIngredient([FromBody]int id)
        {
            HttpRequestHeaders headers = this.Request.Headers;
            string app_key = string.Empty;
            if (headers.Contains("app_key"))
            {
                app_key = headers.GetValues("app_key").First();
            }
            if (headers.Contains("id"))
            {                
                id = int.Parse(headers.GetValues("id").First());
            }
            AuthApp app = await db.AuthApps.Where(a => a.auth_key.Equals(app_key)).FirstAsync();
            if (app == null)
            {
                return NotFound();
            }

            Ingredient ingredient = await db.Ingredients.FindAsync(id);
            if (ingredient == null)
            {
                return NotFound();
            }

            db.Ingredients.Remove(ingredient);
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

        private bool IngredientExists(int id)
        {
            return db.Ingredients.Count(e => e.id == id) > 0;
        }
    }
}