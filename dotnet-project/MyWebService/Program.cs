using Microsoft.AspNetCore.Builder;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;

var builder = WebApplication.CreateBuilder(args);

// Thêm dịch vụ vào container.
builder.Services.AddControllers(); // Thêm dịch vụ cho các Controller

// Thêm dịch vụ OpenAPI/Swagger để tài liệu hóa API
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Tạo ứng dụng
var app = builder.Build();

// Cấu hình pipeline HTTP request.
if (app.Environment.IsDevelopment())
{
    // Sử dụng Swagger và Swagger UI trong môi trường phát triển
    app.UseSwagger();
    app.UseSwaggerUI();
}

// Sử dụng HTTPS Redirection
app.UseHttpsRedirection();

// Sử dụng xác thực (Authorization)
app.UseAuthorization();

// Map các Controller
app.MapControllers();

// Chạy ứng dụng
app.Run();
