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
    public class RecipesController : ApiController
    {
        private ServiceModels db = new ServiceModels();

        // GET: api/Recipes
        public async Task<IHttpActionResult> GetRecipes()
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
            return Ok(await db.Recipes.ToListAsync());
        }

        // GET: api/Recipes/5
        [ResponseType(typeof(Recipe))]
        public async Task<IHttpActionResult> GetRecipe(int id)
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
            Recipe recipe = await db.Recipes.FindAsync(id);
            if (recipe == null)
            {
                return NotFound();
            }

            return Ok(recipe);
        }

        // PUT: api/Recipes/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutRecipe([FromBody]int id, [FromBody]Recipe recipe)
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
                if (!RecipeExists(id))
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

        // POST: api/Recipes
        [ResponseType(typeof(Recipe))]
        public async Task<IHttpActionResult> PostRecipe( [FromBody]Recipe recipe)
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

            db.Recipes.Add(recipe);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = recipe.id }, recipe);
        }

        // DELETE: api/Recipes/5
        [ResponseType(typeof(Recipe))]
        public async Task<IHttpActionResult> DeleteRecipe( [FromBody]int id)
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

            Recipe recipe = await db.Recipes.FindAsync(id);
            if (recipe == null)
            {
                return NotFound();
            }

            db.Recipes.Remove(recipe);
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

        private bool RecipeExists(int id)
        {
            return db.Recipes.Count(e => e.id == id) > 0;
        }
    }
}