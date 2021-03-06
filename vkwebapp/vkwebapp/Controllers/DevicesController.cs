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
    public class DevicesController : ApiController
    {
        private ServiceModels db = new ServiceModels();

        // GET: api/Devices
        public async Task<IHttpActionResult> GetDevices()
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

            return Ok(await db.Devices.ToListAsync());
        }

        // GET: api/Devices/5
        [ResponseType(typeof(Device))]
        public async Task<IHttpActionResult> GetDevice(int id)
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
            Device device = await db.Devices.FindAsync(id);
            if (device == null)
            {
                return NotFound();
            }

            return Ok(device);
        }

        // PUT: api/Devices/5
        [ResponseType(typeof(void))]
        public async Task<IHttpActionResult> PutDevice([FromBody]int id, [FromBody]Device device)
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
                if (!DeviceExists(id))
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
        [ResponseType(typeof(Device))]
        public async Task<IHttpActionResult> PostDevice([FromBody]Device device)
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

            db.Devices.Add(device);
            await db.SaveChangesAsync();

            return CreatedAtRoute("DefaultApi", new { id = device.id }, device);
        }

        // DELETE: api/Devices/5
        [ResponseType(typeof(Device))]
        public async Task<IHttpActionResult> DeleteDevice( [FromBody]int id)
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

            Device device = await db.Devices.FindAsync(id);
            if (device == null)
            {
                return NotFound();
            }

            db.Devices.Remove(device);
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

        private bool DeviceExists(int id)
        {
            return db.Devices.Count(e => e.id == id) > 0;
        }
    }
}