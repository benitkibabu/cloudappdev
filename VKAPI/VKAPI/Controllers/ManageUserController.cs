using System;
using System.Collections.Generic;
using System.Data;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Net;
using System.Web;
using System.Web.Mvc;
using VKAPI.Models;

namespace VKAPI.Controllers
{
    public class ManageclientuserController : Controller
    {
        private VkitchenContext db = new VkitchenContext();

        // GET: Manageclientuser
        public async Task<ActionResult> Index()
        {
            return View(await db.clientusers.ToListAsync());
        }

        // GET: Manageclientuser/Details/5
        public async Task<ActionResult> Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            clientuser clientuser = await db.clientusers.FindAsync(id);
            if (clientuser == null)
            {
                return HttpNotFound();
            }
            return View(clientuser);
        }

        // GET: Manageclientuser/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: Manageclientuser/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Create([Bind(Include = "id,logintype,clientuserid,name,email,imageurl")] clientuser clientuser)
        {
            if (ModelState.IsValid)
            {
                db.clientusers.Add(clientuser);
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }

            return View(clientuser);
        }

        // GET: Manageclientuser/Edit/5
        public async Task<ActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            clientuser clientuser = await db.clientusers.FindAsync(id);
            if (clientuser == null)
            {
                return HttpNotFound();
            }
            return View(clientuser);
        }

        // POST: Manageclientuser/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Edit([Bind(Include = "id,logintype,clientuserid,name,email,imageurl")] clientuser clientuser)
        {
            if (ModelState.IsValid)
            {
                db.Entry(clientuser).State = EntityState.Modified;
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }
            return View(clientuser);
        }

        // GET: Manageclientuser/Delete/5
        public async Task<ActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            clientuser clientuser = await db.clientusers.FindAsync(id);
            if (clientuser == null)
            {
                return HttpNotFound();
            }
            return View(clientuser);
        }

        // POST: Manageclientuser/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> DeleteConfirmed(int id)
        {
            clientuser clientuser = await db.clientusers.FindAsync(id);
            db.clientusers.Remove(clientuser);
            await db.SaveChangesAsync();
            return RedirectToAction("Index");
        }

        protected override void Dispose(bool disposing)
        {
            if (disposing)
            {
                db.Dispose();
            }
            base.Dispose(disposing);
        }
    }
}
