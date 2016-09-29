/* Fragment.js

	Purpose:

	Description:

	History:
		Tue Aug 22 16:50:29 2016, Created by wenninghsu

Copyright (C) 2016 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
/**
 * A fragment widget
 * @since 8.0.3
 */
zul.wgt.Fragment = zk.$extends(zul.Widget, {
	_content: '',
	_fragment: '',
	$define: {
		content: function (v) {
			var n = this.$n();
			if (n) n.innerHTML = v || '';
		},
		fragment: function (v) {
			var n = this.$n();
			if (n) this._setChilrenAttribute();
		}
	},
	_setChilrenAttribute: function () {
		var a = JSON.parse(this._fragment);
		for (var j in a) {
			var uuid = this.uuid,
				uuidIndex = j.indexOf(uuid),
				id = '[' + uuid + '=' + j.substring(uuidIndex + uuid.length) + ']',
				elms = document.querySelectorAll(id),
				attr = j.substring(0, uuidIndex);
			for (var i = 0; i < elms.length; i++) {
				if (attr.startsWith('on')) {
					continue;
				}
				elms[i][attr] = a[j];
				if ('INPUT' == elms[i].tagName) {
					this.domListen_(elms[i], 'onChange', '_fragmentCommand');
				}
			}
		}
	},
	_fragmentCommand: function (evt) {
		console.log('_fragmentCommand');
		// var binder = this.$binder(),
		var uuid = this.uuid,
			name = evt.name,
			elementId = evt.domTarget.getAttribute(uuid),
			value;
		if ('onChange' == name) {
			value = evt.domTarget.value;
		}
		// value = evt.domTarget.getAttribute(attr);
		// binder.command('fragment', {'id': this.uuid, 'command': 'change'});
		zAu.send(new zk.Event(this, name, {element: elementId, value: value}));
	},
	bind_: function () {
		this.$supers(zul.wgt.Fragment, 'bind_', arguments);
		if (jq.isArray(this._content)) { //zk().detachChildren() is used
		    // var bindingAnno = ['@bind', '@command', '@converter', '@global-command', ''];
		    var json = JSON.parse(this._fragment),
				uuid = this.uuid;
			for (var ctn = this._content, n = this.$n(), j = 0; j < ctn.length; ++j) {
				if ('IF' == ctn[j].tagName) {
					if (json['test' + uuid + ctn[j].getAttribute(uuid)]) {
						for (var c = ctn[j].children, i = 0; i < ctn[j].children.length; ++i) {
							n.appendChild(c[i]);
						}
					}
					continue;
				}
				n.appendChild(ctn[j]);
			}
			this._setChilrenAttribute();
			// var a = JSON.parse(this._fragment);
			// for (var j in a) {
			// 	var uuid = this.uuid,
			// 		uuidIndex = j.indexOf(uuid),
			// 		id = '[' + uuid + '=' + j.substring(j.indexOf(uuid) + uuid.length) + ']',
			// 		elms = document.querySelectorAll(id),
			// 		attr = j.substring(0, uuidIndex);
			// 	for (var i = 0; i < elms.length; i++) {
			// 		if (attr.startsWith('on')) {
			// 			var n = this.$n();
			// 			// this.domListen_(n, attr, '_fragmentCommand');
			// 			this.domListen_(elms[i], attr, '_fragmentCommand');
			// 		}
			// 	}
			// }
        }
	},
	unbind_: function () {
		if (jq.isArray(this._content)) //zk().detachChildren() is used
			for (var n = this.$n(); n.firstChild;)
				n.removeChild(n.firstChild);
		this.$supers(zul.wgt.Fragment, 'unbind_', arguments);
	}

});
