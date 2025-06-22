(function (global, factory) {
	typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports, require('@cropper/element'), require('@cropper/element-canvas'), require('@cropper/element-image'), require('@cropper/element-shade'), require('@cropper/element-handle'), require('@cropper/element-selection'), require('@cropper/element-grid'), require('@cropper/element-crosshair'), require('@cropper/element-viewer')) :
	typeof define === 'function' && define.amd ? define(['exports', '@cropper/element', '@cropper/element-canvas', '@cropper/element-image', '@cropper/element-shade', '@cropper/element-handle', '@cropper/element-selection', '@cropper/element-grid', '@cropper/element-crosshair', '@cropper/element-viewer'], factory) :
	(global = typeof globalThis !== 'undefined' ? globalThis : global || self, factory(global.CropperElements = {}, global.CropperElement, global.CropperCanvas, global.CropperImage, global.CropperShade, global.CropperHandle, global.CropperSelection, global.CropperGrid, global.CropperCrosshair, global.CropperViewer));
})(this, (function (exports, element, elementCanvas, elementImage, elementShade, elementHandle, elementSelection, elementGrid, elementCrosshair, elementViewer) { 'use strict';

	function _interopDefaultLegacy (e) { return e && typeof e === 'object' && 'default' in e ? e : { 'default': e }; }

	var element__default = /*#__PURE__*/_interopDefaultLegacy(element);
	var elementCanvas__default = /*#__PURE__*/_interopDefaultLegacy(elementCanvas);
	var elementImage__default = /*#__PURE__*/_interopDefaultLegacy(elementImage);
	var elementShade__default = /*#__PURE__*/_interopDefaultLegacy(elementShade);
	var elementHandle__default = /*#__PURE__*/_interopDefaultLegacy(elementHandle);
	var elementSelection__default = /*#__PURE__*/_interopDefaultLegacy(elementSelection);
	var elementGrid__default = /*#__PURE__*/_interopDefaultLegacy(elementGrid);
	var elementCrosshair__default = /*#__PURE__*/_interopDefaultLegacy(elementCrosshair);
	var elementViewer__default = /*#__PURE__*/_interopDefaultLegacy(elementViewer);



	Object.defineProperty(exports, 'CropperElement', {
		enumerable: true,
		get: function () { return element__default["default"]; }
	});
	Object.defineProperty(exports, 'CropperCanvas', {
		enumerable: true,
		get: function () { return elementCanvas__default["default"]; }
	});
	Object.defineProperty(exports, 'CropperImage', {
		enumerable: true,
		get: function () { return elementImage__default["default"]; }
	});
	Object.defineProperty(exports, 'CropperShade', {
		enumerable: true,
		get: function () { return elementShade__default["default"]; }
	});
	Object.defineProperty(exports, 'CropperHandle', {
		enumerable: true,
		get: function () { return elementHandle__default["default"]; }
	});
	Object.defineProperty(exports, 'CropperSelection', {
		enumerable: true,
		get: function () { return elementSelection__default["default"]; }
	});
	Object.defineProperty(exports, 'CropperGrid', {
		enumerable: true,
		get: function () { return elementGrid__default["default"]; }
	});
	Object.defineProperty(exports, 'CropperCrosshair', {
		enumerable: true,
		get: function () { return elementCrosshair__default["default"]; }
	});
	Object.defineProperty(exports, 'CropperViewer', {
		enumerable: true,
		get: function () { return elementViewer__default["default"]; }
	});

	Object.defineProperty(exports, '__esModule', { value: true });

}));
