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
    public class UsersController : ApiController
    {
        private VkitchenContext db = new VkitchenContext();

        // GET: api/Users
        public async Task<IHttpActionResult> Getusers()
        {
            return Ok(await db.clientusers.ToListAsync());
        }

        // GET: api/Users/5
        [ResponseType(typeof(clientuser))]
        public async Task<IHttpActionResult> Getuser(int id)
        {
            clientuser clientuser = await db.clientusers.FindAsync(id);
            if (clientuser == null)
            {
                return NotFound();
            }

            return Ok(clientuser);
        }

        // PUT: api/Users/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> Putuser(int id, clientuser clientuser)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != clientuser.id)
            {
                return BadRequest();
            }

            db.Entry(clientuser).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!userExists(id))
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
        [ResponseType(typeof(clientuser))]
        public async Task<IHttpActionResult> Postuser([FromBody]clientuser clientuser)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (userExists(clientuser.userid))
            {
                return Ok(await db.clientusers.Where(a => a.userid == clientuser.userid).FirstAsync());
            }else
            {
                db.clientusers.Add(clientuser);
                await db.SaveChangesAsync();

                return CreatedAtRoute("DefaultApi", new { id = clientuser.id }, clientuser);
            }
           
        }

        // DELETE: api/Users/5
        [ResponseType(typeof(clientuser))]
        public async Task<IHttpActionResult> Deleteuser(int id)
        {
            clientuser clientuser = await db.clientusers.FindAsync(id);
            if (clientuser == null)
            {
                return NotFound();
            }

            db.clientusers.Remove(clientuser);
            await db.SaveChangesAsync();

            return Ok(clientuser);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool userExists(int id)
        {
            return db.clientusers.Count(e => e.id == id) > 0;
        }
        private bool userExists(string userid)
        {
            return db.clientusers.Count(e => e.userid == userid) > 0;
        }
    }
}