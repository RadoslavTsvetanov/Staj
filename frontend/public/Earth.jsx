import React, { useRef, useState } from 'react'
import { useGLTF } from '@react-three/drei'
import { useFrame } from '@react-three/fiber';

export default function Model(props) {
  const { nodes, materials, scene } = useGLTF('/earth.gltf');
  
  const earthRef = useRef(null);
  const [isInteracting, setInteracting] = useState(false);
  
  useFrame(() => {
    if (!isInteracting && earthRef.current) {
        earthRef.current.rotation.y += 10.01;
    }
});
  return (
    <group {...props} dispose={null}>
      <mesh geometry={nodes.Object_4.geometry} material={materials['Scene_-_Root']} scale={1.128} />
      <mesh
          ref={earthRef}
          onPointerOver={() => setInteracting(true)}
          onPointerOut={() => setInteracting(false)}
        >
          <primitive object={scene} />
      </mesh>
    </group>
  );
}

useGLTF.preload('/earth.gltf')
