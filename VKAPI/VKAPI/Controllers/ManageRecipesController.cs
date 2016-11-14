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
    public class ManageRecipesController : Controller
    {
        private virtualkitchendbEntities db = new virtualkitchendbEntities();

        // GET: ManageRecipes
        public async Task<ActionResult> Index()
        {
            var recipes = db.recipes.Include(r => r.user);
            return View(await recipes.ToListAsync());
        }

        // GET: ManageRecipes/Details/5
        public async Task<ActionResult> Details(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            recipe recipe = await db.recipes.FindAsync(id);
            if (recipe == null)
            {
                return HttpNotFound();
            }
            return View(recipe);
        }

        // GET: ManageRecipes/Create
        public ActionResult Create()
        {
            ViewBag.userid = new SelectList(db.users, "id", "logintype");
            return View();
        }

        // POST: ManageRecipes/Create
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Create([Bind(Include = "id,uri,label,imageurl,source,url,shareas,yield,dietlabel,healthlabel,caution,ingredientlines,calories,totalweight,userid")] recipe recipe)
        {
            if (ModelState.IsValid)
            {
                db.recipes.Add(recipe);
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }

            ViewBag.userid = new SelectList(db.users, "id", "logintype", recipe.userid);
            return View(recipe);
        }

        // GET: ManageRecipes/Edit/5
        public async Task<ActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            recipe recipe = await db.recipes.FindAsync(id);
            if (recipe == null)
            {
                return HttpNotFound();
            }
            ViewBag.userid = new SelectList(db.users, "id", "logintype", recipe.userid);
            return View(recipe);
        }

        // POST: ManageRecipes/Edit/5
        // To protect from overposting attacks, please enable the specific properties you want to bind to, for 
        // more details see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Edit([Bind(Include = "id,uri,label,imageurl,source,url,shareas,yield,dietlabel,healthlabel,caution,ingredientlines,calories,totalweight,userid")] recipe recipe)
        {
            if (ModelState.IsValid)
            {
                db.Entry(recipe).State = EntityState.Modified;
                await db.SaveChangesAsync();
                return RedirectToAction("Index");
            }
            ViewBag.userid = new SelectList(db.users, "id", "logintype", recipe.userid);
            return View(recipe);
        }

        // GET: ManageRecipes/Delete/5
        public async Task<ActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return new HttpStatusCodeResult(HttpStatusCode.BadRequest);
            }
            recipe recipe = await db.recipes.FindAsync(id);
            if (recipe == null)
            {
                return HttpNotFound();
            }
            return View(recipe);
        }

        // POST: ManageRecipes/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> DeleteConfirmed(int id)
        {
            recipe recipe = await db.recipes.FindAsync(id);
            db.recipes.Remove(recipe);
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
