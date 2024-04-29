/* Copyright (C) 2016 Tcl Corporation Limited */
#include "CardboardMesh.h"

namespace android {

CardboardMesh::CardboardMesh(Primitive primitive, size_t vertexCount, size_t vertexSize, size_t texCoordSize)
		: mVertexCount(vertexCount), mVertexSize(vertexSize), mTexCoordsSize(texCoordSize),
		  mPrimitive(primitive){
	if (vertexCount == 0) {
		mVertices = new float[1];
		mVertices[0] = 0.0f;
		mStride = 0;
		return;
	}

	size_t stride = vertexSize + texCoordSize;
	size_t remainder = (stride * vertexCount) / vertexCount;
	// Since all of the input parameters are unsigned, if stride is less than
	// either vertexSize or texCoordSize, it must have overflowed. remainder
	// will be equal to stride as long as stride * vertexCount doesn't overflow.
	if ((stride < vertexSize) || (remainder != stride)) {
		mVertices = new float[1];
		mVertices[0] = 0.0f;
		mVertexCount = 0;
		mVertexSize = 0;
		mTexCoordsSize = 0;
		mStride = 0;
		return;
	}

	mVertices = new float[stride * vertexCount];
	mStride = stride;
}

CardboardMesh::~CardboardMesh() {
	delete [] mVertices;
}

float* CardboardMesh::getPositions() {
	return mVertices;
}

float* CardboardMesh::getTexCoords() {
	return mVertices + mVertexSize;
}

}
