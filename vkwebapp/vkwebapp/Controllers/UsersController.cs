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
using vkwebapp.Models;

namespace vkwebapp.Controllers
{
    public class UsersController : ApiController
    {
        private ServiceModels db = new ServiceModels();

        // GET: api/Users
        public async Task<IHttpActionResult> GetClientUsers([FromBody]string app_key)
        {
            AuthApp app = await db.AuthApps.Where(a => a.auth_key.Equals(app_key)).FirstAsync();
            if (app == null)
            {
                return NotFound();
            }

            return Ok(await db.ClientUsers.ToListAsync());
        }

        // GET: api/Users/5
        [ResponseType(typeof(ClientUser))]
        public async Task<IHttpActionResult> GetClientUser([FromBody]string app_key, [FromBody]int id)
        {
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
        public async Task<IHttpActionResult> PutClientUser([FromBody]string app_key, [FromBody]int id, [FromBody]ClientUser clientUser)
        {
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
        public async Task<IHttpActionResult> PostClientUser([FromBody]string app_key, [FromBody]ClientUser clientUser)
        {
            AuthApp app = await db.AuthApps.Where(a => a.auth_key.Equals(app_key)).FirstAsync();
            if (app == null)
            {
                return NotFound();
            }

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.ClientUsers.Add(clientUser);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = clientUser.id }, clientUser);
        }

        // DELETE: api/Users/5
        [ResponseType(typeof(ClientUser))]
        public async Task<IHttpActionResult> DeleteClientUser([FromBody]string app_key, [FromBody]int id)
        {
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
    }
}