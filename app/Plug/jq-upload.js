$(function() {
	$(document).on("change",".upload-img-input", function() {
		if(this.files.length===0) return;
		var file=this.files;
		if(!/(gif|jpg|jpeg|bmp|png)$/.test(this.files[0].type)){
			layer.msg('The suffix does not meet the requirements');return false;
		}
		var uploadLayer=layer.load(1);
		var _this = $(this);
		var fr = new FileReader();
		fr.readAsDataURL(this.files[0]);
		var img = new Image();
		var btn = _this.parent();
		fr.onload = function() {
			img.src = this.result;
			img.onload = function() {
				btn.siblings(".upload-img").html(img);
				layer.close(uploadLayer);
			};
		};
	});
});
