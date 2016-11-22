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
    public class DevicesController : ApiController
    {
        private VkitchenContext db = new VkitchenContext();

        // GET: api/Devices
        public async Task<IHttpActionResult> Getdevices()
        {
            return Ok( await db.devices.ToListAsync());
        }

        // GET: api/Devices/5
        [ResponseType(typeof(device))]
        public async Task<IHttpActionResult> Getdevice(int id)
        {
            device device = await db.devices.FindAsync(id);
            if (device == null)
            {
                return NotFound();
            }

            return Ok(device);
        }

        // PUT: api/Devices/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> Putdevice(int id, device device)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (id != device.id)
            {
                return BadRequest();
            }

            db.Entry(device).State = EntityState.Modified;

            try
            {
                await db.SaveChangesAsync();
            }
            catch (DbUpdateConcurrencyException)
            {
                if (!deviceExists(id))
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

        // POST: api/Devices
        [ResponseType(typeof(device))]
        public async Task<IHttpActionResult> Postdevice(device device)
        {
            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            db.devices.Add(device);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = device.id }, device);
        }

        // DELETE: api/Devices/5
        [ResponseType(typeof(device))]
        public async Task<IHttpActionResult> Deletedevice(int id)
        {
            device device = await db.devices.FindAsync(id);
            if (device == null)
            {
                return NotFound();
            }

            db.devices.Remove(device);
            await db.SaveChangesAsync();

            return Ok(device);
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }

        private bool deviceExists(int id)
        {
            return db.devices.Count(e => e.id == id) > 0;
        }
    }
}