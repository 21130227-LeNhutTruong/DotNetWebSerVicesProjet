using Microsoft.AspNetCore.Mvc;
using System.Collections.Generic;
using models; // Namespace của lớp Product

namespace controllers
{
      [Route("api/[controller]")]
    [ApiController]
    public class ProductsController : ControllerBase
    {
        // Sample data
        private static readonly List<Product> Products = new List<Product>
        {
            new Product { Id = 1, Name = "Product 1", Price = 10.0M },
            new Product { Id = 2, Name = "Product 2", Price = 20.0M },
            new Product { Id = 3, Name = "Product 3", Price = 30.0M }
        };

        // GET: api/products
        [HttpGet]
        public ActionResult<IEnumerable<Product>> GetProducts()
        {
            return Ok(Products);
        }

        // GET: api/products/5
        [HttpGet("{id}")]
        public ActionResult<Product> GetProduct(int id)
        {
            var product = Products.Find(p => p.Id == id);
            if (product == null)
            {
                return NotFound();
            }
            return Ok(product);
        }
    }
}