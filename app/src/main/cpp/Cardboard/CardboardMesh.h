/* Copyright (C) 2016 Tcl Corporation Limited */
#ifndef ANDROID_CARDBOARDMESH_H
#define ANDROID_CARDBOARDMESH_H

#include <stdint.h>
//#include "CardboardPostRender.h"

namespace android {

class CardboardMesh{
public:
	enum Primitive {
		TRIANGLES       = 0x0004,       // GL_TRIANGLES
		TRIANGLE_STRIP  = 0x0005,       // GL_TRIANGLE_STRIP
		TRIANGLE_FAN    = 0x0006        // GL_TRIANGLE_FAN
	};

	CardboardMesh(Primitive primitive, size_t vertexCount, size_t vertexSize, size_t texCoordsSize = 0);
	~CardboardMesh();
	template <typename TYPE>
	class VertexArray {
		friend class Mesh;
		float* mData;
		size_t mStride;
	public:
		TYPE& operator[](size_t index) {
			return *reinterpret_cast<TYPE*>(&mData[index*mStride]);
		}
		TYPE const& operator[](size_t index) const {
			return *reinterpret_cast<TYPE const*>(&mData[index*mStride]);
		}

		VertexArray(float* data, size_t stride) : mData(data), mStride(stride) { }
	};

	template <typename TYPE>
	VertexArray<TYPE> getPositionArray() { return VertexArray<TYPE>(getPositions(), mStride); }

	template <typename TYPE>
	VertexArray<TYPE> getTexCoordArray() { return VertexArray<TYPE>(getTexCoords(), mStride); }

private:
	friend class CardboardPostRender;

	CardboardMesh(const CardboardMesh&);
	CardboardMesh& operator = (const CardboardMesh&);
	CardboardMesh const& operator = (const CardboardMesh&) const;

	float* getPositions();
	float* getTexCoords();
	float* mVertices;
	size_t mVertexCount;
	size_t mVertexSize;
	size_t mTexCoordsSize;
	size_t mStride;
	Primitive mPrimitive;
};

}

#endif
