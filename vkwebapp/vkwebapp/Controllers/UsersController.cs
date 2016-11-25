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
        public IQueryable<ClientUser> GetClientUsers()
        {
            return db.ClientUsers;
        }

        // GET: api/Users/5
        [ResponseType(typeof(ClientUser))]
        public async Task<IHttpActionResult> GetClientUser(int id)
        {
            ClientUser clientUser = await db.ClientUsers.FindAsync(id);
            if (clientUser == null)
            {
                return NotFound();
            }

            return Ok(clientUser);
        }

        // PUT: api/Users/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutClientUser(int id, ClientUser clientUser)
        {
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
        public async Task<IHttpActionResult> PostClientUser(ClientUser clientUser)
        {
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
        public async Task<IHttpActionResult> DeleteClientUser(int id)
        {
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