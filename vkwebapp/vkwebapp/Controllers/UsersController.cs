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
    public class UsersController : ApiController
    {
        private ServiceModels db = new ServiceModels();

        // GET: api/Users
        public async Task<IHttpActionResult> GetClientUsers()
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

            return Ok(await db.ClientUsers.ToListAsync());
        }

        // GET: api/Users/5
        [ResponseType(typeof(ClientUser))]
        public async Task<IHttpActionResult> GetClientUser(int id)
        {
            HttpRequestHeaders headers = this.Request.Headers;
            string app_key = string.Empty;
            if (headers.Contains("app_key"))
            {
                app_key = headers.GetValues("app_key").First();
            }
            AuthApp app = await db.AuthApps.Where(a => a.auth_key.Equals(app_key)).FirstAsync();
            if(app == null)
            {
                return NotFound();
            }

            ClientUser clientUser = await db.ClientUsers.FindAsync(id);
            if (clientUser == null)
            {
                return NotFound();
            }

            return Ok(clientUser);
        }

        // PUT: api/Users/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutClientUser([FromBody]int id, [FromBody]ClientUser clientUser)
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

            if (id != clientUser.id)
            {
                return BadRequest();
            }

            db.Entry(clientUser).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!ClientUserExists(id))
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

        // POST: api/Users
        [ResponseType(typeof(ClientUser))]
        public async Task<IHttpActionResult> PostClientUser( [FromBody]ClientUser clientUser)
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

            if (ClientUserExists(clientUser.email))
            {
                return Ok(db.ClientUsers.Where(c=> c.email.Equals(clientUser.email)).First());
            }

            db.ClientUsers.Add(clientUser);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = clientUser.id }, clientUser);
        }

        // DELETE: api/Users/5
        [ResponseType(typeof(ClientUser))]
        public async Task<IHttpActionResult> DeleteClientUser([FromBody]int id)
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
            ClientUser clientUser = await db.ClientUsers.FindAsync(id);
            if (clientUser == null)
            {
                return NotFound();
            }

            db.ClientUsers.Remove(clientUser);
            await db.SaveChangesAsync();

            return Ok(clientUser);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool ClientUserExists(int id)
        {
            return db.ClientUsers.Count(e => e.id == id) > 0;
        }
        private bool ClientUserExists(string email)
        {
            return db.ClientUsers.Count(e => e.email.Equals(email)) > 0;
        }
    }
}